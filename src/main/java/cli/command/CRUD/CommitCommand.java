package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import file.DHTFiles;
import servent.message.AddMessage;
import servent.message.AskPullMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class CommitCommand implements CLICommand {


    /** TODO
        * commit name - Osvežava datoteku name u sistemu za verzioniranje i povećava verziju.
        * Sve datoteke kreću sa verzijom 0, i pri commit operaciji se verzija datoteke inkrementira.
        * Kao name može da se navede i direktorijum, u kojem slučaju se vrši commit operacija rekurzivno za čitav direktorijum.
        * Ako datoteka nije menjana od prethodne verzije, ne treba joj menjati verziju pri commit-ovanju.
        * Ako je datoteka već ažurirana od strane nekog drugog čvora, tj. došlo je do konflikta, pitati korisnika kako želi da razreši konflikt
    * */

    @Override
    public String commandName() {
        return "commit";
    }


    @Override
    public void execute(String args) {
        if(args.contains(".txt")){

        }else{

        }
    }



    private List<File> sendFile(String fullPath, int key, boolean flag){
        AppConfig.timestampedErrorPrint(fullPath);
        if(fullPath.contains(".txt")){
            List<File> listOfFiles = new ArrayList<>();
            File f = new File(fullPath);
            if(flag){
                AddMessage addMsg = new AddMessage(
                        AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                        f,key);
                MessageUtil.sendMessage(addMsg);
            }
            listOfFiles.add(f);
            return listOfFiles;
        }else{
            try {
                List<File> listOfFiles = new ArrayList<>();
                Files.walk(Paths.get(fullPath))
                        .filter(Files::isRegularFile)
                        .forEach(a->{
                            File f = new File(a.toFile().getAbsolutePath());
                            if(flag) {
                                AddMessage addMsg = new AddMessage(
                                        AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                                        f, key);
                                MessageUtil.sendMessage(addMsg);
                            }
                            listOfFiles.add(f);
                        });
                return listOfFiles;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        AppConfig.timestampedStandardPrint("Komanda izvrsena");
        return null;
    }
}