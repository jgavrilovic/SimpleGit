package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import file.GitKey;
import file.LocalStorage;
import servent.message.AddMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AddHandler implements MessageHandler {

    private Message clientMessage;

    public AddHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ADD_GITFILE) {
            int key = ((AddMessage)clientMessage).getTarget();
            if (AppConfig.chordState.isKeyMine(key)) {
                try {
                    File f = ((AddMessage)clientMessage).getFile();

                    String filePath1 = f.getPath().substring(f.getPath().indexOf("localRoot")).substring(10);
                    String filePath2 = AppConfig.myServentInfo.getStoragePath()+"\\"+filePath1;
                    String[] filePath3 = filePath2.split("\\\\");
                    String dirs="";
                    String name = "";
                    for(int i =0 ; i<filePath3.length;i++){
                        if(i<filePath3.length-1){
                            dirs=dirs+filePath3[i]+"\\";
                        }else{
                            name=filePath3[i].substring(0,filePath3[i].length()-4)+"0.txt";
                        }

                    }
                    //pravimo dir
                    boolean b =new File(dirs).mkdirs();
                    AppConfig.timestampedStandardPrint("premesten direktorijum: " + b);

                    //premestamo fileove
                    AppConfig.timestampedStandardPrint(dirs+"  " + name + "   " +  dirs+name);
                    Files.move(Paths.get(f.getAbsolutePath()), Paths.get(dirs+name));


                    LocalStorage.storage.add(new GitFile(name,new File(dirs+name),0));
                }catch (Exception e){
                    e.printStackTrace();
                }


            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                AddMessage addm = new AddMessage(
                        clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        ((AddMessage)clientMessage).getFile(),key
                );
                MessageUtil.sendMessage(addm);
            }
        } else {
            AppConfig.timestampedErrorPrint("Add handler got a message that is not ADDFILE");
        }

    }
}