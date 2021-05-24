package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import cli.command.CRUD.AddCommand;
import file.GitFile;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.CRUD.AskPullMessage;
import servent.message.CRUD.TellPullMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TEAM_COMM.TellPullTeamMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class AskPullHandler  implements MessageHandler {

    private Message clientMessage;

    public AskPullHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ASK_PULL) {
            int key = ((AskPullMessage)clientMessage).getTarget();
            //proverava se krajnja destinacija
            if (AppConfig.chordState.isKeyMine(key)) {

                try{
                    //cita se iz poruke ime i verzija
                    String name = clientMessage.getMessageText().split(" ")[0]; //jovan.txt 5 || dir1 -2
                    int version = Integer.parseInt(clientMessage.getMessageText().split(" ")[1]);

                    AppConfig.timestampedStandardPrint("Dosao je zahtev za povlacenje datoteke pod nazivom: " + name + " i verzije: " + version);

                    //trazi se najveca moguca verzija
                    int maxVersion;
                    if(version==-2){
                        maxVersion = LocalStorage.storage.stream().filter(x -> x.getName().equals(name)).mapToInt(GitFile::getVersion).filter(o -> o >= 0).max().orElse(0);
                    }else {
                        maxVersion=version;
                    }

                    //u listu fajlova se dodaju oni koji od govaraju zahtevima
                    List<GitFile> gitfiles = new ArrayList<>();
                    int finalMaxVersion = maxVersion;
                    if(name.contains(".txt")){
                        LocalStorage.storage.stream().filter(x->x.getName().equals(name) && x.getVersion()== finalMaxVersion).forEach(gitfiles::add);
                        //------------------------------------part2
                        LocalStorage.storage.stream().filter(x->x.getName().equals(name) && x.getVersion()== finalMaxVersion).forEach(git->{
                            git.getTeamPulls().entrySet().stream().filter(e->e.getKey().equals(((AskPullMessage) clientMessage).getTeam())).forEach(team->{
                                git.getTeamPulls().put(team.getKey(),team.getValue()+1);
                            });
                        });
                        //------------------------------------
                    }
                    else{
                        LocalStorage.storage.stream().filter(x->x.getPath().contains(name)).forEach(gitfiles::add);
                        //------------------------------------part2
                        LocalStorage.storage.stream().filter(x->x.getName().equals(name)).forEach(git->{
                            git.getTeamPulls().entrySet().stream().filter(e->e.getKey().equals(((AskPullMessage) clientMessage).getTeam())).forEach(team->{
                                git.getTeamPulls().put(team.getKey(),team.getValue()+1);
                            });
                        });
                        //------------------------------------
                    }

                    //------------------------------------part2
                    //provera da li se granica pull presla
                    LocalStorage.storage.stream().forEach(git->{
                        //za svaku datoteku dohvatamo niz timova i njihovih povlacenja
                        git.getTeamPulls().entrySet().forEach(tim->{
                            //ako se granica presla napravi klon kod njih
                            if(tim.getValue()>AppConfig.myServentInfo.getTeamLimit()){
                                //dakle saljem file_name,file_content,file_version kod cvora iz tima koji je presao granicu
                                //on to prihvata preko handlera cuva u svoj storage i clanovima tima salje BOOM flag
                                //boom flag u pull_file menja destinaciju ako je u pitanju ta datoteka
                                TellPullTeamMessage tptm = new TellPullTeamMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(),
                                        git.getName().replaceAll("[0-9]\\.","."), AddCommand.fileReader(git.getPath()), git.getVersion());
                                MessageUtil.sendMessage(tptm);
                            }
                        });
                    });

                    //------------------------------------
                    /**TODO ako dodje novi cvor mora da mu se dostavi za koje dadtoteke vaze pravila za pull*/

                    AppConfig.timestampedStandardPrint("Lista odgovarajucih fajlova za slanje: " + gitfiles.toString());
                    for (GitFile file: gitfiles) {
                        TellPullMessage tgm = new TellPullMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(),
                                file.getPath(), AddCommand.fileReader(file.getPath()), file.getVersion()); //src/../storgae/a0.txt
                        MessageUtil.sendMessage(tgm);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                AskPullMessage agm = new AskPullMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        clientMessage.getMessageText(),((AskPullMessage)clientMessage).getTarget(),((AskPullMessage) clientMessage).getTeam());
                MessageUtil.sendMessage(agm);
            }
        } else {
            AppConfig.timestampedErrorPrint("Ask handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}