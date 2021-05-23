package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.CRUD.AddMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AddHandler implements MessageHandler {

    private Message clientMessage;

    public AddHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ADD_GITFILE) {

            ArrayList<GitFile> templist= new ArrayList<>();
            int key = ((AddMessage)clientMessage).getTarget();

            //provera da li sam ja kranji cvor , ili dalje prosledjujem poruku
            if (AppConfig.chordState.isKeyMine(key)) {
                try {
                    //citam ime i sadrzinu fajla
                    String nameFile = ((AddMessage)clientMessage).getNameOfFile();
                    String contentFile = ((AddMessage)clientMessage).getContentOfFile();

                    //ime fajla se cuvao kao ime + verzija
                    nameFile=nameFile.substring(0,nameFile.length()-4)+"0.txt";

                    //odvajam putanju do direkorijuma od putanje do fajla
                    String fullpath = AppConfig.myServentInfo.getStoragePath()+"/"+nameFile;
                    String[] arrayFullpath = fullpath.split("\\\\");
                    StringBuilder dirs= new StringBuilder();

                    for(int i=0;i<arrayFullpath.length-1;i++){
                        dirs.append(arrayFullpath[i]).append("/");
                    }

                    //pravim foldere
                    boolean a = new File(dirs.toString()).mkdirs();
                    if(a){
                        AppConfig.timestampedStandardPrint("Uspesno kreirani folderi do fajla");
                    }else{
                        AppConfig.timestampedStandardPrint("Folder vec postoji!");
                    }

                    //pravim fajl
                    File f = new File(fullpath);
                    if(f.isFile()){
                        boolean b  = f.createNewFile();
                        AppConfig.timestampedStandardPrint("Potvrda da je file uspesno kreidan: " +b);
                    }

                    //upisujem sadrzinu fajla u novi fajl
                    try {
                        FileWriter myWriter = new FileWriter(f);
                        myWriter.write(contentFile);
                        myWriter.close();
                        AppConfig.timestampedStandardPrint("Uspeno upisivanje sadrzaja u fajl");
                    } catch (IOException e) {
                        AppConfig.timestampedErrorPrint("Doslo je do greske pri upisivanju");
                        e.printStackTrace();
                    }

                    //dodajem gitfile(name,verzija) u storage
                    GitFile gitFile = new GitFile(f.getPath());
                    LocalStorage.storage.add(gitFile);
//
//                    //dodajem gitfile u DHT tabelu!
//                    templist.add(gitFile);
//                    if(!DHTFiles.dhtFiles.containsKey(new GitKey(AppConfig.myServentInfo.getChordId()))){
//                        DHTFiles.dhtFiles.put(new GitKey(AppConfig.myServentInfo.getChordId()), new ArrayList<>(templist));
//                    }else {
//                        for (Map.Entry<GitKey, List<GitFile>> entry0: DHTFiles.dhtFiles.entrySet()) {
//                            if(entry0.getKey().getRandNumber()==AppConfig.myServentInfo.getChordId()){
//                                DHTFiles.dhtFiles.get(entry0.getKey()).addAll(templist);
//                            }
//                        }
//                    }


                }catch (Exception e){
                    AppConfig.timestampedErrorPrint("Doslo je do greske: ");
                    e.printStackTrace();
                }


            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                AddMessage addm = new AddMessage(
                        clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        ((AddMessage)clientMessage).getNameOfFile(),((AddMessage)clientMessage).getContentOfFile(),
                        ((AddMessage) clientMessage).getSender(),key
                );
                MessageUtil.sendMessage(addm);
            }
        } else {
            AppConfig.timestampedErrorPrint("Add handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}