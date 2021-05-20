package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.CRUD.AskPullMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.CRUD.TellPullMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class AskPullHandler  implements MessageHandler {

    private Message clientMessage;

    public AskPullHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ASK_PULL) {
            int key = ((AskPullMessage)clientMessage).getTarget();
            if (AppConfig.chordState.isKeyMine(key)) {
                try {

                    /**mozda treba da se popravi jer 2 puta salje 2 poruke*/

                    String name = clientMessage.getMessageText().split(" ")[0];
                    int version = Integer.parseInt(clientMessage.getMessageText().split(" ")[1]);
                    List<GitFile> gitfiles = new ArrayList<>();
                    LocalStorage.storage.stream().filter(x->x.getName().equals(name) && x.getVersion()==version).forEach(gitfiles::add);

                    AppConfig.timestampedStandardPrint(gitfiles.toString());
                    for (GitFile file: gitfiles) {
                      //  if(file.getFile().getPath().contains(name) && file.getVersion()==version){
                            TellPullMessage tgm = new TellPullMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(), file);
                            MessageUtil.sendMessage(tgm);
                      //  }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                AskPullMessage agm = new AskPullMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        clientMessage.getMessageText(),((AskPullMessage)clientMessage).getTarget());
                MessageUtil.sendMessage(agm);
            }
        } else {
            AppConfig.timestampedErrorPrint("ASK_PULL get handler got a message that is not ASK_PULL");
        }

    }
}