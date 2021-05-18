package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import file.DHTFiles;
import file.GitFile;
import file.GitKey;
import file.LocalStorage;
import servent.message.AddMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class AddHandler implements MessageHandler {

    private Message clientMessage;

    public AddHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }
    /**TODO
     *  adsadasd
     *
     * */
    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ADD_GITFILE) {
            int key = ((AddMessage)clientMessage).getTarget();
            if (AppConfig.chordState.isKeyMine(key)) {

                AppConfig.timestampedErrorPrint(AppConfig.myServentInfo.getStoragePath());
                File f=null;
                try {
                     f = ((AddMessage)clientMessage).getFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
                AppConfig.timestampedErrorPrint(f.getName());
                try {
                    File finalF = f;

                    Files.walk(Paths.get(AppConfig.myServentInfo.getStoragePath()))
                            .forEach(a-> {
                                AppConfig.timestampedErrorPrint(a.toString());
                                if(!finalF.getName().equals(a.toFile().getName())){

                                    AppConfig.timestampedErrorPrint(finalF.getName() + " se premesta!");
                                    List<GitFile> list = new ArrayList<>();
                                    try {
                                        String newName = finalF.getName();
                                        newName =newName.substring(0,newName.length()-4)+"0.txt";
                                        Files.move(Paths.get(finalF.getAbsolutePath()), Paths.get(AppConfig.myServentInfo.getStoragePath()+"/"+ newName));

                                        if(LocalStorage.storage.containsKey(new GitKey(key))){
                                            list.addAll(LocalStorage.storage.get(new GitKey(key)));
                                        }

                                        list.add(new GitFile(newName, new File(AppConfig.myServentInfo.getStoragePath()+"/"+ newName)));
                                        AppConfig.timestampedErrorPrint(list.toString());
                                        LocalStorage.storage.put(new GitKey(key),list);
                                        DHTFiles.dhtFiles.put(new GitKey(key),list);
                                        AppConfig.timestampedErrorPrint("------------------------");
                                        AppConfig.timestampedErrorPrint(LocalStorage.storage.toString());
                                        AppConfig.timestampedErrorPrint(DHTFiles.dhtFiles.toString());
                                    } catch (Exception e) {
                                        AppConfig.timestampedErrorPrint("Datoteka se ne nalazi na njegovom radnom korenu!");
                                    }

                                }else{
                                    AppConfig.timestampedErrorPrint("Datoteka " + finalF.getName() + " vec postoji u sistemu!");
                                }
                            });
                } catch (IOException e) {
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