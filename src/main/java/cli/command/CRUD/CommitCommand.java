package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import file.DHTFiles;
import file.GitFile;
import file.GitKey;
import file.LocalRoot;
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
import java.util.Map;

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

        String fullPath = AppConfig.myServentInfo.getRootPath()+"/"+args;



        if(args.contains(".txt")){
            File myFile = new File(fullPath);
            for (Map.Entry<GitKey,List<GitFile>> gitFile: LocalRoot.workingRoot.entrySet()) {
                if (gitFile.getKey().getRandNumber()==AppConfig.myServentInfo.getChordId()){
                    gitFile.getValue().stream().filter(x-> x.getName().equals(args)).forEach(f->{
                        if(f.getFile().lastModified()!=myFile.lastModified()){
                            //povecaj verziju i commituj
                        }else{
                            //commituj sa istom verzijom
                        }
                    });
                }
            }
        }else{

        }
    }


}