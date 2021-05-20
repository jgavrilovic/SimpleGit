package servent.message.CRUD;

import file.GitFile;
import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;

import java.io.File;

@Getter
public class CommitMessage extends BasicMessage {


    private static final long serialVersionUID = -2144883641345474574L;
    private int senderID;
    private int target;
    private GitFile gitFile;

    public CommitMessage(int senderPort, int receiverPort, GitFile gitFile, int senderID ,int target) {
        super(MessageType.COMMIT, senderPort, receiverPort);
        this.gitFile=gitFile;
        this.senderID=senderID;
        this.target=target;
    }

}
