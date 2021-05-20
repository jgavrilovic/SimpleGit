package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import servent.handler.MessageHandler;
import servent.message.CRUD.AskPullMessage;
import servent.message.CRUD.ConflictMessage;
import servent.message.CRUD.ConflictType;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ConflictHandler  implements MessageHandler {

    private Message clientMessage;
    public ConflictHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }
    /**
     *
     * posaljem @ConflictMessage biraj opcije, null
     * vrati mi view - posaljem komanda view, gitgile()
     * vrati mi push - posaljem DONE_PUSH, null ->upisem njegovu
     * vrati mi pull - posaljem DONE_Pull, gitfile() -> update
     *
     * */

    public static int fromTO =0;
    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.CONFLICT) {
            try {

                int key = ((ConflictMessage)clientMessage).getTarget();
                if (AppConfig.chordState.isKeyMine(key)) {
                    ConflictType txt = ((ConflictMessage)clientMessage).getConflictType();

                    if(txt==ConflictType.WARNING){
                        AppConfig.timestampedStandardPrint(txt + " doslo je do konflikta...");
                    }else{

                    }

                    fromTO=((ConflictMessage)clientMessage).getSenderID();
                }else {
                    ConflictMessage conflictMessage = new ConflictMessage(
                            AppConfig.myServentInfo.getListenerPort(),
                            AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                            ((ConflictMessage)clientMessage).getConflictType(),
                            ((ConflictMessage)clientMessage).getGitFile(),
                            ((ConflictMessage)clientMessage).getSenderID(), key);
                    MessageUtil.sendMessage(conflictMessage);
                }

            }  catch (Exception e) {
              e.printStackTrace();
            }
        } else {
            AppConfig.timestampedErrorPrint("ConflictHandler got a message that is not CONFLICT");
        }
    }

}