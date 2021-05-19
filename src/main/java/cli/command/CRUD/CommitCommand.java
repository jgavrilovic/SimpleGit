package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import file.*;
import servent.handler.TellPullHandler;
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
        String fullPath;
        if(args!=null){
            fullPath = AppConfig.myServentInfo.getRootPath()+"/"+args;
        }else{
            fullPath = AppConfig.myServentInfo.getRootPath();
        }




        if(fullPath.contains(".txt")){
            AppConfig.timestampedStandardPrint("komituje se file " + fullPath);
            File myFile = new File(fullPath);
            for (Map.Entry<GitKey,List<GitFile>> gitFile: LocalRoot.workingRoot.entrySet()) {
                    gitFile.getValue().stream().filter(x -> x.getFile().getPath().contains(args)).forEach(f -> {
                        if (TellPullHandler.lastModifiedTimeFiles.get(myFile) != myFile.lastModified()) {
                            System.out.println(f.getFile().lastModified() + " 1 " + myFile.lastModified());
                        } else {
                            System.out.println(f.getFile().lastModified() + " 2 " + myFile.lastModified());
                        }
                    });

            }
        }else{
            AppConfig.timestampedStandardPrint("komituje se dir: " + fullPath);

            try {
                Files.walk(Paths.get(fullPath))
                        .filter(Files::isRegularFile)
                        .forEach(a->{
                            File myFile = new File(a.toFile().getPath());
                            for (Map.Entry<GitKey,List<GitFile>> gitFile: LocalRoot.workingRoot.entrySet()) {
                                    gitFile.getValue().stream().filter(x -> x.getFile().getPath().contains(args)).forEach(f -> {
                                        if (TellPullHandler.lastModifiedTimeFiles.get(myFile) != myFile.lastModified()) {
                                            System.out.println(f.getFile().lastModified() + " 1 " + myFile.lastModified());
                                        } else {
                                            System.out.println(f.getFile().lastModified() + " 2 " + myFile.lastModified());
                                        }
                                    });
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

//LocalRoot: {GitKey(randNumber=48)=
// [GitFile(name=tri00.txt, file=src\main\resources\servent0\localRoot\tri0.txt, version=0),
// GitFile(name=dva00.txt, file=src\main\resources\servent0\localRoot\dir1\dva0.txt, version=0),
// GitFile(name=jedan00.txt, file=src\main\resources\servent0\localRoot\dir1\jedan0.txt, version=0)]}

//16:48:52 - LastModified: {
// src\main\resources\servent0\localRoot\dir1\jedan000.txt=1621435571736,
// src\main\resources\servent0\localRoot\dir1\dva000.txt=1621435570869,
// src\main\resources\servent0\localRoot\tri000.txt=1621435542988}
