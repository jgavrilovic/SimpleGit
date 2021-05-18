package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import file.GitKey;
import file.LocalStorage;
import servent.message.AskPullMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellPullMessage;
import servent.message.util.MessageUtil;

import java.util.List;

public class AskPullHandler  implements MessageHandler {

    private Message clientMessage;

    public AskPullHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ASK_PULL) {
            AppConfig.timestampedErrorPrint("usao u ask_pull handler");
            int key = ((AskPullMessage)clientMessage).getTarget();
            if (AppConfig.chordState.isKeyMine(key)) {
                AppConfig.timestampedErrorPrint("za mene je logika!");

                try {
                    String name = clientMessage.getMessageText().split(" ")[0];
                    int version = Integer.parseInt(clientMessage.getMessageText().split(" ")[1]);

                    List<GitFile> g = LocalStorage.storage.get(new GitKey(key));
                    AppConfig.timestampedErrorPrint("pronasao ! " + g);

                    for (GitFile gs: g) {
                        if(gs.getName().equals(name) && gs.getVersion()==version){
                            AppConfig.timestampedErrorPrint("saljem tell poruku " + gs.getName() + " i verziju " + gs.getVersion());
                            TellPullMessage tgm = new TellPullMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(),
                                    gs);
                            MessageUtil.sendMessage(tgm);
                        }
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