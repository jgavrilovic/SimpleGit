package servent.handler;

import app.AppConfig;
import file.GitFile;
import file.GitKey;
import file.LocalRoot;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellPullMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class TellPullHandler  implements MessageHandler {

    private Message clientMessage;

    public TellPullHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    /**TODO
     *  Ako sam ja add datoteku i kazem pull
     *      1-verzija ista kao kod mene, ispisi da nema potrebe
     *      2-verzija razlicitia ili najnovija, dostavi je (logika stoji da je ime datoteke = ime+vezija)
     *
     *
     * */

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.TELL_PULL) {

            //dohvatim datoteku
            GitFile gitFile = ((TellPullMessage)clientMessage).getFile();
            File f = gitFile.getFile();
            //napravim novu datoteku na radnom korenu pod istim nazivom
            File newf = new File(AppConfig.myServentInfo.getRootPath()+"/"+gitFile.getName());


            try {
                //kreiram je
                boolean isCreated = newf.createNewFile();
                if(isCreated){
                    AppConfig.timestampedStandardPrint("File je kreiran na vas radni koren: " + newf.getName());
                }

                //iscitam datoteku koju sam dobio
                Scanner myReader = new Scanner(gitFile.getFile());
                StringBuilder stringBuilder = new StringBuilder();
                while (myReader.hasNextLine()) {
                    stringBuilder.append(myReader.next()).append(" ");
                }
                myReader.close();

                //prepisem njen sadrzaj u novu datoteku koju sam napravio
                FileWriter myWriter = new FileWriter(newf.getAbsolutePath());
                myWriter.write(stringBuilder.toString());
                myWriter.close();

                AppConfig.timestampedStandardPrint(gitFile.getName()+"je dostavljena u vas radni koren!");
            } catch (FileNotFoundException e) {
                AppConfig.timestampedErrorPrint("Datoteka nije pronadjena! " + e.getMessage());
            }  catch (IOException e) {
                AppConfig.timestampedErrorPrint("Datoteka ne moze da se otvori! " + e.getMessage());
            }
            List<GitFile> fileList = new ArrayList<>();
            if(LocalRoot.workingRoot.containsKey(new GitKey(AppConfig.myServentInfo.getChordId()))){
                fileList.addAll(LocalRoot.workingRoot.get(new GitKey(AppConfig.myServentInfo.getChordId())));
            }
            fileList.add(new GitFile(newf.getName(), newf, gitFile.getVersion()));
            LocalRoot.workingRoot.put(new GitKey(AppConfig.myServentInfo.getChordId()), fileList);
        } else {
            AppConfig.timestampedErrorPrint("Tell get handler got a message that is not TELL_GET");
        }
    }

}