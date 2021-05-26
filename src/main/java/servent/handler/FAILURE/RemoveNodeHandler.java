package servent.handler.FAILURE;

import app.AppConfig;
import servent.handler.MessageHandler;
import servent.message.FAILURE.RemoveNodeMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

public class RemoveNodeHandler implements MessageHandler {

    private Message clientMessage;

    public RemoveNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.REMOVE_NODE) {

            int key = ((RemoveNodeMessage)clientMessage).getTarget();

            if (AppConfig.myServentInfo.getChordId()==key) {

                    AppConfig.timestampedStandardPrint("zavrsila se reorganizacija");

            } else {
                /**REMOVE NODE*/
                int id = ((RemoveNodeMessage)clientMessage).getRemoveID();
                AppConfig.timestampedStandardPrint("Brise se node: " + id);
                AppConfig.chordState.removeNodes(id);
                RemoveNodeMessage removeNodeMessage = new RemoveNodeMessage(
                        AppConfig.myServentInfo.getListenerPort(),
                        AppConfig.chordState.getNextNodePort(),
                        id,
                        ((RemoveNodeMessage) clientMessage).getTarget()
                );
                MessageUtil.sendMessage(removeNodeMessage);
            }
        } else {
            AppConfig.timestampedErrorPrint("RemoveNode handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}
