package servent.message.TEAM_ORG;

import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class TeamMessage extends BasicMessage {

    private static final long serialVersionUID = -5614361189961750701L;

    private int teamID;
    private String teamLetter;
    private int target;
    private ConcurrentHashMap<String, Set<Integer>> teams;
    public TeamMessage(int senderPort, int receiverPort, int teamID, String teamLetter, int target, ConcurrentHashMap<String, Set<Integer>> teams) {
        super(MessageType.TEAM_UPDATE, senderPort, receiverPort);
        this.teamID=teamID;
        this.teamLetter=teamLetter;
        this.target = target;
        this.teams= teams;
    }

    @Override
    public String toString() {
        return "TeamMessage: " + super.getSenderPort()+" | "+super.getReceiverPort()+" | "+super.getMessageId()+" | "+teamLetter+" | "+ teams+" | "+target;
    }
}
