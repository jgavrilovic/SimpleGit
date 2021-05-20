package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import file.LocalStorage;
import org.apache.commons.io.FileUtils;
import servent.message.CommitMessage;
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

    /*
      GitFile(name=a0.txt, file=src\main\resources\servent0\localRoot\a0.txt, version=1)
      GitFile(name=a0.txt, file=src\main\resources\servent0\localRoot\a0.txt, version=0)
    * GitFile(name=b00.txt, file=src\main\resources\servent0\localRoot\dir1\b00.txt, version=0)
    * GitFile(name=c00.txt, file=src\main\resources\servent0\localRoot\dir1\c00.txt, version=0)
    * */

    /*
     * GitFile(name=a0.txt, file=src\main\resources\servent1\localStorage\a0.txt, version=0),
     * GitFile(name=b00.txt, file=src\main\resources\servent1\localStorage\dir1\b00.txt, version=0),
     * GitFile(name=c00.txt, file=src\main\resources\servent1\localStorage\dir1\c00.txt, version=0)]
     */
    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.COMMIT) {
            int key = ((CommitMessage)clientMessage).getTarget();
            if (AppConfig.chordState.isKeyMine(key)) {
                try {
                    //GitFile(name=a0.txt, file=src\main\resources\servent0\localRoot\a0.txt, version=0)
                    //GitFile(name=a0.txt, file=src\main\resources\servent0\localRoot\a0.txt, version=1)
                    GitFile gitFile = ((CommitMessage)clientMessage).getGitFile();

                    Queue<GitFile> temp = new ConcurrentLinkedQueue<>();
                    LocalStorage.storage.stream().filter(f-> f.getName().equals(gitFile.getName())).iterator().forEachRemaining(o->{
                        if(o.getVersion()==gitFile.getVersion()){
                            AppConfig.timestampedStandardPrint("ista verzija datoteke");
                        }else{
                            //src\main\resources\servent0\localRoot\a1.txt
                            String path = gitFile.getFile().getPath().substring(0,gitFile.getFile().getPath().length()-5)+gitFile.getVersion()+".txt";
                            path=path.replace(path.substring(0,path.indexOf("localRoot")+9),AppConfig.myServentInfo.getStoragePath());
                            AppConfig.timestampedErrorPrint("Dobro? " + path);
                            File file = new File(path);
                            try {
                                FileUtils.copyFile(gitFile.getFile(), new File(path));
                                boolean isCreated = file.createNewFile();
                                if(isCreated){
                                    AppConfig.timestampedStandardPrint("Dodata nova datoteka u sistem " + file );
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            temp.add(new GitFile(gitFile.getName(),file,gitFile.getVersion()));

                        }
                    });

                    LocalStorage.storage.addAll(temp);
                    temp.clear();

                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                CommitMessage agm = new CommitMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        ((CommitMessage)clientMessage).getGitFile(),((CommitMessage)clientMessage).getTarget());
                MessageUtil.sendMessage(agm);
            }
        } else {
            AppConfig.timestampedErrorPrint("CommitHandler got a message that is not COMMIT");
        }

    }
}