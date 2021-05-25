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
import team.LocalTeam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class AddHandler implements MessageHandler {

    private Message clientMessage;

    public AddHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ADD_GITFILE) {

            int key = ((AddMessage)clientMessage).getTarget();

            //provera da li sam ja kranji cvor , ili dalje prosledjujem poruku
            if (AppConfig.chordState.isKeyMine(key)) {
                try {
                    //citam ime i sadrzinu fajla
                    String nameFile = ((AddMessage)clientMessage).getNameOfFile(); //jovan.txt || dir1/jovan.txt
                    String contentFile = ((AddMessage)clientMessage).getContentOfFile();
                    String path=nameFile.replace(".","0.");
                    //ime fajla se cuvao kao ime + verzija



                    //odvajam putanju do direkorijuma od putanje do fajla
                    String fullpath = AppConfig.myServentInfo.getStoragePath()+"/"+path; //src/../jovan0.txt
                    String[] arrayFullpath = fullpath.split("\\\\");
                    StringBuilder dirs= new StringBuilder();

                    for(int i=0;i<arrayFullpath.length-1;i++){
                        dirs.append(arrayFullpath[i]).append("/");
                    }


                    AppConfig.timestampedStandardPrint(nameFile);
                    //pravim foldere
                    boolean a = new File(dirs.toString()).mkdirs();
                    if(a){
                        AppConfig.timestampedStandardPrint("Uspesno kreirani folderi do fajla");
                    }else{
                        AppConfig.timestampedStandardPrint("Folder vec postoji!");
                    }

                    //pravim fajl
                    AppConfig.timestampedStandardPrint("pravim fajl na putanji: " + fullpath);
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
                    //za svaku datoteku inicijaluzjem 'team'='0'
                    ConcurrentHashMap<String,Integer> teamPulls = new ConcurrentHashMap<>();
                    LocalTeam.teams.entrySet().stream().forEach(e->{
                        teamPulls.put(e.getKey(),0);
                    });
                    //dodajem gitfile(name,verzija) u storage
                    GitFile gitFile = new GitFile(nameFile,f.getPath(),0,teamPulls);
                    LocalStorage.storage.add(gitFile);

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