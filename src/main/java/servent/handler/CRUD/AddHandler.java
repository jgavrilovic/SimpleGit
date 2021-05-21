package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import file.DHTFiles;
import file.GitFile;
import file.GitKey;
import file.LocalStorage;
import org.apache.commons.io.FileUtils;
import servent.handler.MessageHandler;
import servent.message.CRUD.AddMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.CRUD.UpdateDHTMessage;
import servent.message.util.MessageUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddHandler implements MessageHandler {

    private Message clientMessage;

    public AddHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    /**ovde moras da dobijes ime fajla i sadrzinu njegovu txt*/
    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ADD_GITFILE) {
            ArrayList<GitFile> templist= new ArrayList<>();
            int sender = ((AddMessage)clientMessage).getSender();
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

                    File ff = new File(dirs+name);

                    FileUtils.copyFile(ff, new File(f.getPath()));

                    templist.add(new GitFile(name,ff,0));
                    LocalStorage.storage.add(new GitFile(name,ff,0));


                    if(!DHTFiles.dhtFiles.containsKey(new GitKey(AppConfig.myServentInfo.getChordId()))){
                        DHTFiles.dhtFiles.put(new GitKey(AppConfig.myServentInfo.getChordId()), new ArrayList<>(templist));
                    }else {
                        for (Map.Entry<GitKey, List<GitFile>> entry0: DHTFiles.dhtFiles.entrySet()) {
                            if(entry0.getKey().getRandNumber()==AppConfig.myServentInfo.getChordId()){
                                DHTFiles.dhtFiles.get(entry0.getKey()).addAll(templist);
                            }
                        }
                    }


                    UpdateDHTMessage updateDHTMessage = new UpdateDHTMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodePort(),
                            new GitFile(name,ff,0), AppConfig.myServentInfo.getChordId());
                    MessageUtil.sendMessage(updateDHTMessage);
                }catch (Exception e){
                    e.printStackTrace();
                }


            } else {

                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                AddMessage addm = new AddMessage(
                        clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        ((AddMessage)clientMessage).getFile(),((AddMessage) clientMessage).getSender(),key
                );
                MessageUtil.sendMessage(addm);
            }
        } else {
            AppConfig.timestampedErrorPrint("Add handler got a message that is not ADDFILE");
        }

    }
}