package servent.message;

import lombok.Getter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class TeamMessage extends BasicMessage {

    private static final long serialVersionUID = -5614361189961750701L;

    private int teamID;
    private String teamLetter;
    private int originalSender;
    private ConcurrentHashMap<String, Set<Integer>> teams;
    public TeamMessage(int senderPort, int receiverPort,int teamID,String teamLetter,int originalSender, ConcurrentHashMap<String, Set<Integer>> teams) {
        super(MessageType.TEAM_UPDATE, senderPort, receiverPort);
        this.teamID=teamID;
        this.teamLetter=teamLetter;
        this.originalSender=originalSender;
        this.teams= teams;
    }

    @Override
    public String toString() {
        return "TeamMessage{" +
                "teamID=" + teamID +
                ", teamLetter='" + teamLetter + '\'' +
                ", originalSender=" + originalSender +
                ", teams=" + teams +
                '}';
    }
}
