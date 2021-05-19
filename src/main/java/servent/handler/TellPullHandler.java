package servent.handler;

import app.AppConfig;
import file.GitFile;
import file.GitKey;
import file.LocalRoot;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellPullMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class TellPullHandler  implements MessageHandler {

    private Message clientMessage;
    public static ConcurrentHashMap<File,Long> lastModifiedTimeFiles = new ConcurrentHashMap<>();
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
            try {
                //dohvatim datoteku
                GitFile gitFile = ((TellPullMessage)clientMessage).getFile();
                AppConfig.timestampedStandardPrint(gitFile+"");
                String path = gitFile.getFile().getPath();
                String fp =path.substring(path.indexOf("localStorage")).substring(13);
                String pp = fp.substring(0,fp.length()-4)+"0.txt";


                //iscitam datoteku koju sam dobio
                System.out.println(0);
                FileReader fr=new FileReader(gitFile.getFile());   //reads the file
                BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
                StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
                String line;
                while((line=br.readLine())!=null)
                {
                    sb.append(line).append(" ");      //appends line to string buffer
                }
                fr.close();    //closes the stream and release the resources


                System.out.println(1);
                //napravim novu datoteku na radnom korenu pod istim nazivom
                File newf = new File(AppConfig.myServentInfo.getRootPath()+"/"+pp);
                boolean isCreated = newf.createNewFile();
                if(isCreated){
                    AppConfig.timestampedStandardPrint("File je kreiran na vas radni koren: " + newf.getName());
                }

                lastModifiedTimeFiles.put(newf,newf.lastModified());
                System.out.println(2);
                for (Map.Entry<GitKey, List<GitFile>> entry: LocalRoot.workingRoot.entrySet()) {
                    LocalRoot.workingRoot.get(entry.getKey()).add(new GitFile(newf.getName(),newf,0));
                }

                System.out.println(3);
                //prepisem njen sadrzaj u novu datoteku koju sam napravio
                FileWriter myWriter = new FileWriter(newf);
                myWriter.write(sb.toString());
                myWriter.close();
                System.out.println(4);
                AppConfig.timestampedStandardPrint(gitFile.getName()+" je dostavljena u vas radni koren!");


            } catch (FileNotFoundException e) {
                AppConfig.timestampedErrorPrint("Datoteka nije pronadjena! " + e.getMessage());
                e.printStackTrace();
            }  catch (IOException e) {
                AppConfig.timestampedErrorPrint("Datoteka ne moze da se otvori! " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            AppConfig.timestampedErrorPrint("Tell get handler got a message that is not TELL_PULL");
        }
    }

}