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
                String fileName = ((TellPullMessage)clientMessage).getNameOfFile();
                String fileContent = ((TellPullMessage)clientMessage).getContentOfFile();
                AppConfig.timestampedStandardPrint(fileName+"");

                /**TODO ej problem je kada 1 satvi u 2 a 3 povuce iz 2 jer nema vec napravljen dir!*/

                //napravim novu datoteku na radnom korenu pod istim nazivom
                String newPath = fileName.substring(fileName.indexOf("localStorage")).replace("localStorage","");
                File newf = new File(AppConfig.myServentInfo.getRootPath()+newPath);
                boolean isCreated = newf.createNewFile();
                if(isCreated){
                    AppConfig.timestampedStandardPrint("File je kreiran na vas radni koren: " + newf.getName() + ((TellPullMessage) clientMessage).getVersion());
                }

                //dodaje se datoteka na radni koren
                LocalRoot.workingRoot.add(new GitFile(newf.getPath(),((TellPullMessage) clientMessage).getVersion()));


                //prepisem njen sadrzaj u novu datoteku koju sam napravio
                FileWriter myWriter = new FileWriter(newf);
                myWriter.write(fileContent);
                myWriter.close();

                lastModifiedTimeFiles.put(newf,newf.lastModified());
                AppConfig.timestampedStandardPrint(fileName+" je dostavljena u vas radni koren!");


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