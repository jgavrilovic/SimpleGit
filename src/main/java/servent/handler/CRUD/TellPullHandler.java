package servent.handler.CRUD;

import app.AppConfig;
import file.GitFile;
import file.LocalRoot;
import servent.handler.MessageHandler;
import servent.message.CRUD.TellPullMessage;
import servent.message.Message;
import servent.message.MessageType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TellPullHandler  implements MessageHandler {

    private Message clientMessage;
    public static ConcurrentHashMap<File,Long> lastModifiedTimeFiles = new ConcurrentHashMap<>();
    public TellPullHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.TELL_PULL) {
            try {
                //dohvatim naziv i sadrzaj datoteke

                String fullpath = ((TellPullMessage)clientMessage).getNameOfFile();  //src/../storage/jovan0.txt | src/.../storage/dir1/b0.txt
                String fileContent = ((TellPullMessage)clientMessage).getContentOfFile();
                AppConfig.timestampedStandardPrint("Prihvatio naziv fajla: " + fullpath);

                fullpath=fullpath.substring(fullpath.indexOf("localStorage")).replace("localStorage\\",""); //jovan0.txt || dir1/b0.txt
                fullpath=AppConfig.myServentInfo.getRootPath()+"/"+fullpath; //src/../root/jovan0.txt | src/.../root/dir1/b0.txt

                //odvajam putanju do direkorijuma od putanje do fajla
                AppConfig.timestampedStandardPrint("Nova putanja do fajla za pull handler: " + fullpath);
                String[] arrayFullpath = fullpath.split("\\\\");
                StringBuilder dirs= new StringBuilder();

                for(int i=0;i<arrayFullpath.length;i++){
                    if(i<arrayFullpath.length-1)
                        dirs.append(arrayFullpath[i]).append("/");
                }

                //pravim foldere
                boolean a = new File(dirs.toString()).mkdirs();
                if(a){
                    AppConfig.timestampedStandardPrint("Uspesno kreirani folderi do fajla");
                }else{
                    AppConfig.timestampedStandardPrint("Folder vec postoji!");
                }

                //napravim novu datoteku na radnom korenu pod istim nazivom
                File f = new File(fullpath);
                if(f.isFile()){
                    boolean b  = f.createNewFile();
                    AppConfig.timestampedStandardPrint("Potvrda da je file "+f+" uspesno kreidan: " +b);
                }


                //dodaje se datoteka na radni koren
                LocalRoot.workingRoot.add(new GitFile(f.getName(),f.getPath(),((TellPullMessage) clientMessage).getVersion()));


                //prepisem njen sadrzaj u novu datoteku koju sam napravio
                FileWriter myWriter = new FileWriter(f);
                myWriter.write(fileContent);
                myWriter.close();

                lastModifiedTimeFiles.put(f,f.lastModified());
                AppConfig.timestampedStandardPrint(f.getName()+" je dostavljena u vas radni koren!");


            } catch (FileNotFoundException e) {
                AppConfig.timestampedErrorPrint("Datoteka nije pronadjena! " + e.getMessage());
                e.printStackTrace();
            }  catch (IOException e) {
                AppConfig.timestampedErrorPrint("Datoteka ne moze da se otvori! " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            AppConfig.timestampedErrorPrint("Tell handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }
    }

}