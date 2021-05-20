package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import file.LocalStorage;
import org.apache.commons.io.FileUtils;
import servent.handler.MessageHandler;
import servent.message.CRUD.CommitMessage;
import servent.message.CRUD.ConflictMessage;
import servent.message.CRUD.ConflictType;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class CommitHandler implements MessageHandler {

    private Message clientMessage;

    public CommitHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.COMMIT) {
            int key = ((CommitMessage)clientMessage).getTarget();
            if (AppConfig.chordState.isKeyMine(key)) {
                try {
                    GitFile gitFile = ((CommitMessage)clientMessage).getGitFile();

                    Queue<GitFile> temp = new ConcurrentLinkedQueue<>();
                    LocalStorage.storage.stream().filter(f-> f.getName().equals(gitFile.getName())).iterator().forEachRemaining(o->{
                        if(o.getVersion()==gitFile.getVersion()){
                            AppConfig.timestampedStandardPrint("ista verzija datoteke");
                        }else{

                            String path = gitFile.getFile().getPath().substring(0,gitFile.getFile().getPath().length()-5)+gitFile.getVersion()+".txt";
                            path=path.replace(path.substring(0,path.indexOf("localRoot")+9),AppConfig.myServentInfo.getStoragePath());

                            File file = new File(path);
                            try {
                                FileUtils.copyFile(gitFile.getFile(), new File(path));
                                boolean isCreated = file.createNewFile();
                                if(isCreated){
                                    AppConfig.timestampedStandardPrint("Dodata nova datoteka u sistem " + file );
                                }
                            } catch (IOException e) {
                                AppConfig.timestampedErrorPrint("Doslo je do problema pri kopiranju ili kreiranju fajla " +e.getMessage() );
                            }
                            temp.add(new GitFile(gitFile.getName(),file,gitFile.getVersion()));

                        }
                    });




                    GitFile gitFile1 = LocalStorage.storage.stream().iterator().next();
                    GitFile gitFile2 = temp.element();
                    if(gitFile2.getName().equals(gitFile1.getName()) && gitFile2.getFile().getPath().equals(gitFile1.getFile().getPath()) && gitFile2.getVersion()==gitFile1.getVersion()){

                        int target  = ((CommitMessage)clientMessage).getSenderID();
                        ConflictMessage conflictMessage = new ConflictMessage(
                                AppConfig.myServentInfo.getListenerPort(),
                                AppConfig.chordState.getNextNodeForKey(target).getListenerPort(),
                                ConflictType.WARNING,
                                null,
                                AppConfig.myServentInfo.getChordId(), target
                        );
                        MessageUtil.sendMessage(conflictMessage);

                    }else {
                        LocalStorage.storage.addAll(temp);
                        temp.clear();
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                CommitMessage agm = new CommitMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        ((CommitMessage)clientMessage).getGitFile(),((CommitMessage)clientMessage).getSenderID(),((CommitMessage)clientMessage).getTarget());
                MessageUtil.sendMessage(agm);
            }
        } else {
            AppConfig.timestampedErrorPrint("CommitHandler got a message that is not COMMIT");
        }

    }
}