package servent.handler.TEAM_ORG;

import app.AppConfig;
import servent.handler.MessageHandler;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TEAM_ORG.TeamMessage;
import servent.message.util.MessageUtil;
import team.LocalTeam;

import java.util.HashSet;
import java.util.Set;

public class TeamHandler implements MessageHandler {

    private Message clientMessage;

    public TeamHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.TEAM_UPDATE) {
            int sender = ((TeamMessage)clientMessage).getOriginalSender();
            if (AppConfig.myServentInfo.getChordId()==sender) {

                LocalTeam.teams= ((TeamMessage) clientMessage).getTeams();
                AppConfig.timestampedErrorPrint("ne saljem: " + LocalTeam.teams.toString());
            } else {

                try {
                    AppConfig.timestampedErrorPrint("usao u else");
                    AppConfig.timestampedErrorPrint("iz poruke: " + ((TeamMessage) clientMessage).getTeams());
                    ((TeamMessage) clientMessage).getTeams().entrySet().stream().forEach(e->{
                        Set<Integer> unique = new HashSet<>(((TeamMessage) clientMessage).getTeams().get(e.getKey()));
                        if(LocalTeam.teams.containsKey(e.getKey()))
                            unique.addAll(LocalTeam.teams.get(e.getKey()));

                        AppConfig.timestampedErrorPrint("cela mapa koja staje u tims: " + unique.toString());
                        LocalTeam.teams.put(e.getKey(),unique);
                    });
                    AppConfig.timestampedErrorPrint("ovo se salje kruzno: " + LocalTeam.teams.toString());

                    TeamMessage teamMessage = new TeamMessage(
                            clientMessage.getSenderPort(),
                            AppConfig.chordState.getNextNodePort(),
                            ((TeamMessage) clientMessage).getTeamID(),
                            ((TeamMessage) clientMessage).getTeamLetter(),
                            ((TeamMessage) clientMessage).getOriginalSender(),
                            LocalTeam.teams
                    );
                    MessageUtil.sendMessage(teamMessage);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        } else {
            AppConfig.timestampedErrorPrint("Team handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}