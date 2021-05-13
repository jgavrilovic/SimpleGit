package servent.handler;

import app.AppConfig;
import app.ChordState;
import app.ServentInfo;
import app.ServentMain;
import cli.command.CLICommand;
import cli.command.StopCommand;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NodeQuitHandler implements MessageHandler {

    private Message clientMessage;
    private boolean flag = true;
    public static Set<Message> receivedQuit = Collections.newSetFromMap(new ConcurrentHashMap<Message, Boolean>());
    public NodeQuitHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    /**
     * Ako sam ja prvo koji dobija poruku:
     *      -Preuzimam njegovu tabelu podataka
     *      -Preuzimam za koje cvorove je bio odgovoran
     *      -Preuzimam njegovog prethodnika
     *      -updatetujem succsesor tabelu
     *
     * Ako sam ja bilo ko drugi:
     *      -updatetujem succsesor tabelu
     *
     * Ako sam ja taj koji se brise:
     *      -Dobijam nazad update poruku(izvrteo se krug)
     *      -Ugasim se
     *
     * */
    //successor_info

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.QUIT) {
            try {
                boolean didPut = receivedQuit.add(clientMessage);
                if (didPut) {

                    //saljem svima poruku samo bez ValueMap jer nju samo moj prvi sledbenik treba da osvezi
                    if(flag){
                        AppConfig.timestampedErrorPrint("Saljem dalje: " + AppConfig.chordState.getNextNodePort());
                        Message nextUpdate = new NodeQuitMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePort(),
                                clientMessage.getMessageText(),null,AppConfig.myServentInfo.getChordId(), clientMessage.get
                                );
                        MessageUtil.sendMessage(nextUpdate);
                        flag=false;
                    }


                    //port - id - port(mog prethodnika)
                    String msgTxt = clientMessage.getMessageText();
                    int port = Integer.parseInt(msgTxt.split(" ")[0]);
                    int id =  Integer.parseInt(msgTxt.split(" ")[1]);
                    int pred  = Integer.parseInt(msgTxt.split(" ")[2]);
                    ServentInfo info = new ServentInfo("localhost", port);
                    ServentInfo infoPred = new ServentInfo("localhost", pred);

                    //prvi sam koji je dobio poruku
                    if(AppConfig.chordState.getPredecessor().getChordId()==id){
                        AppConfig.timestampedErrorPrint("usao1");
                        AppConfig.chordState.getValueMap().putAll(clientMessage.getValueMap());
                        AppConfig.chordState.removeNodes(info);
                        AppConfig.chordState.setPredecessor(infoPred);
                    }else{
                        AppConfig.timestampedErrorPrint("usao2");
                        AppConfig.chordState.removeNodes(info);
                    }


                    if(AppConfig.myServentInfo.getChordId()==id){
                        AppConfig.timestampedErrorPrint("usao3");
                        System.exit(0);
                    }



                } else {
                    AppConfig.timestampedStandardPrint("Already had this. No rebroadcast.");
                }




            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            AppConfig.timestampedErrorPrint("QUIT handler got a message that is not QUIT");
        }

    }
}
