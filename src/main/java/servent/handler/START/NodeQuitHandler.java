package servent.handler.START;

import app.AppConfig;
import app.ServentInfo;
import servent.handler.MessageHandler;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.START.NodeQuitMessage;
import servent.message.util.MessageUtil;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NodeQuitHandler implements MessageHandler {

    private Message clientMessage;
    private boolean flag = true;
    public static Set<Message> receivedQuit = Collections.newSetFromMap(new ConcurrentHashMap<>());
    public NodeQuitHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

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
                                clientMessage.getMessageText(),null);
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
                        AppConfig.chordState.getValueMap().putAll(clientMessage.getValueMap());
                        AppConfig.chordState.removeNodes(info);
                        AppConfig.chordState.setPredecessor(infoPred);
                    }else{
                        AppConfig.chordState.removeNodes(info);
                    }


                    if(AppConfig.myServentInfo.getChordId()==id){
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
