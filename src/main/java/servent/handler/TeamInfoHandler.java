package servent.handler;

import app.AppConfig;
import cli.command.CRUD.PullCommand;
import servent.message.Message;
import servent.message.MessageInfoTeam;
import servent.message.MessageType;
import servent.message.util.MessageUtil;


public class TeamInfoHandler implements MessageHandler {

    private Message clientMessage;

    public TeamInfoHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.TEAM_INFO) {
            int key = ((MessageInfoTeam)clientMessage).getTarget();
            if(AppConfig.myServentInfo.getChordId()==key){
                AppConfig.timestampedStandardPrint("Poruka o datoteci je za mene: " + ((MessageInfoTeam) clientMessage).getGitFile());
                PullCommand.listaTimskihFajlova.put(((MessageInfoTeam) clientMessage).getIdKodKogaJe(),((MessageInfoTeam) clientMessage).getGitFile());
            }else{
                MessageInfoTeam messageInfoTeam = new MessageInfoTeam(
                        AppConfig.myServentInfo.getListenerPort(),
                        AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                        ((MessageInfoTeam) clientMessage).getGitFile(),
                        ((MessageInfoTeam) clientMessage).getTarget(),
                        ((MessageInfoTeam) clientMessage).getIdKodKogaJe()

                );
                MessageUtil.sendMessage(messageInfoTeam);
            }




        } else {
            AppConfig.timestampedErrorPrint("TeamInfo handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}