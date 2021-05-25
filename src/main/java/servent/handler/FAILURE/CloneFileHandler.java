package servent.handler.FAILURE;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.CRUD.AddMessage;
import servent.message.FAILURE.CloneFileMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;
import team.LocalTeam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CloneFileHandler implements MessageHandler {

    private Message clientMessage;

    public CloneFileHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.CLONE_GITFILE) {

            int sender = ((CloneFileMessage)clientMessage).getSender();
            int key = ((CloneFileMessage)clientMessage).getTarget();
            
            if (AppConfig.chordState.isKeyMine(key)) {
                try {
                        String name = ((CloneFileMessage)clientMessage).getNameOfFile();
                        String content = ((CloneFileMessage)clientMessage).getContentOfFile();
                        GitFile gitFile = ((CloneFileMessage)clientMessage).getGitFile();

                        AppConfig.timestampedStandardPrint("OVO JE IZ CLONE HANDLER:");
                        AppConfig.timestampedStandardPrint(name+" "+content+" "+gitFile);

                        LocalStorage.storage.add(gitFile);


                        /**TODO napravi jos te fajlove*/

                }catch (Exception e){
                    AppConfig.timestampedErrorPrint("Doslo je do greske: ");
                    e.printStackTrace();
                }

            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                CloneFileMessage cloneFileMessage = new CloneFileMessage(
                        clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        ((CloneFileMessage)clientMessage).getNameOfFile(),
                        ((CloneFileMessage)clientMessage).getContentOfFile(),
                        ((CloneFileMessage)clientMessage).getGitFile(),
                        ((CloneFileMessage) clientMessage).getSender(),key
                );
                MessageUtil.sendMessage(cloneFileMessage);
            }
        } else {
            AppConfig.timestampedErrorPrint("CloneFile handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}