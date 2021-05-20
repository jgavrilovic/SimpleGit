package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import file.DHTFiles;
import file.GitFile;
import file.LocalRoot;
import servent.handler.CRUD.TellPullHandler;
import servent.message.CRUD.CommitMessage;
import servent.message.util.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            sendCommitMessage(myFile);
        }else{
            AppConfig.timestampedStandardPrint("komituje se dir: " + fullPath);

            try {
                Files.walk(Paths.get(fullPath))
                        .filter(Files::isRegularFile)
                        .forEach(a->{
                            File myFile = new File(a.toFile().getPath());
                            sendCommitMessage(myFile);
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private int key=0;
    private void sendCommitMessage(File myFile) {
        LocalRoot.workingRoot.stream().filter(x->x.getFile().getPath().equals(myFile.getPath())).forEach(f->{
            TellPullHandler.lastModifiedTimeFiles.entrySet().stream().filter(x-> x.getKey().getPath().equals(f.getFile().getPath())).forEach(fs->{
                AppConfig.timestampedStandardPrint(fs.getValue()+"   "+f.getFile().lastModified());
                if(fs.getValue()==f.getFile().lastModified()){

                    DHTFiles.dhtFiles.entrySet().stream().filter(
                            t-> t.getValue().stream().iterator().next().getName().equals(myFile.getName())
                    ).forEach(o->{key=o.getKey().getRandNumber();});
                    AppConfig.timestampedStandardPrint(key+"if");
                    CommitMessage msg = new CommitMessage(
                            AppConfig.myServentInfo.getListenerPort(),
                            AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                            new GitFile(f.getName(),f.getFile(),f.getVersion()),
                            AppConfig.myServentInfo.getChordId(),key);
                    MessageUtil.sendMessage(msg);
                }else {//+1

                    DHTFiles.dhtFiles.entrySet().stream().filter(
                            t-> t.getValue().stream().iterator().next().getName().equals(myFile.getName())
                    ).forEach(o->{key=o.getKey().getRandNumber();});
                    AppConfig.timestampedStandardPrint(key+"else");
                    CommitMessage msg = new CommitMessage(
                            AppConfig.myServentInfo.getListenerPort(),
                            AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                            new GitFile(f.getName(),f.getFile(),f.getVersion()+1),
                            AppConfig.myServentInfo.getChordId(),key);
                    MessageUtil.sendMessage(msg);
                }
            });
        });
    }
}
