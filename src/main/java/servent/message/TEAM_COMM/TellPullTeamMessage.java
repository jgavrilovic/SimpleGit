package servent.message.TEAM_COMM;

import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;

@Getter
public class TellPullTeamMessage extends BasicMessage {


    private static final long serialVersionUID = -5325146676937623841L;
    private String nameOfFile;
    private String contentOfFile;
    private int version;

    public TellPullTeamMessage(int senderPort, int receiverPort, String nameOfFile, String contentOfFile,int version) {
        super(MessageType.TELL_PULL_TEAM, senderPort, receiverPort);
        this.nameOfFile=nameOfFile;
        this.contentOfFile=contentOfFile;
        this.version=version;
    }

    @Override
    public String toString() {
        return "TellPullTeamMessage: " + super.getSenderPort()+" | "+super.getReceiverPort()+" | "+super.getMessageId()+" | "+nameOfFile+" | "+ contentOfFile+" | "+ version;
    }

}
