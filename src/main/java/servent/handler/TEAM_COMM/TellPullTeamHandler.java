package servent.handler.TEAM_COMM;

import app.AppConfig;
import file.GitFile;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.Message;
import servent.message.TEAM_ORG.MessageInfoTeam;
import servent.message.MessageType;
import servent.message.TEAM_COMM.TellPullTeamMessage;
import servent.message.util.MessageUtil;
import team.LocalTeam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TellPullTeamHandler implements MessageHandler {

    private Message clientMessage;
    public TellPullTeamHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.TELL_PULL_TEAM) {
            try {
                String name = ((TellPullTeamMessage)clientMessage).getNameOfFile();
                String content = ((TellPullTeamMessage)clientMessage).getContentOfFile();
                int version = ((TellPullTeamMessage)clientMessage).getVersion();

                AppConfig.timestampedErrorPrint("TeamHandler: " + name+" "+content+" "+version);
                String path = AppConfig.myServentInfo.getStoragePath()+"/"+name.replace(".",version+".");


                //napravim novu datoteku na radnom korenu pod istim nazivom
                File f = new File(path);
                if(f.isFile()){
                    boolean b  = f.createNewFile();
                    AppConfig.timestampedStandardPrint("Potvrda da je file "+f+" uspesno kreidan: " +b);
                }


                //prepisem njen sadrzaj u novu datoteku koju sam napravio
                FileWriter myWriter = new FileWriter(f);
                myWriter.write(content);
                myWriter.close();

                LocalStorage.storage.add(new GitFile(name,f.getPath(),version));

                //sada kada ima kod sebe treba da obavesti sve iz tima da koriste njegovu!
                //prodjem kroz listu timova i javim im ime i verziju datoteke da je kod mene
                //to je dodatna lista u pull command tako da prvo proverimo da li se u njoj nalazi datoteka ako da onda pitamo taj cvor

                //prolazim kroz listu timova i za tim kod koga je replikacija obavestavam clanove tima o nazivu i verziji datoteke
                LocalTeam.teams.entrySet().stream().filter(s->s.getKey().equals(AppConfig.myServentInfo.getTeamName())).forEach(v->{
                    v.getValue().stream().forEach(id->{
                        MessageInfoTeam messageInfoTeam = new MessageInfoTeam(
                                AppConfig.myServentInfo.getListenerPort(),
                                AppConfig.chordState.getNextNodeForKey(id).getListenerPort(),
                                new GitFile(name,f.getPath(),version),
                                id,AppConfig.myServentInfo.getChordId()
                        );
                        MessageUtil.sendMessage(messageInfoTeam);
                    });
                });


            } catch (IOException e) {
                AppConfig.timestampedErrorPrint("Datoteka ne moze da se kreira/procita na putanji: " + e.getMessage());
            }
        } else {
            AppConfig.timestampedErrorPrint("TellTeam handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }
    }

}