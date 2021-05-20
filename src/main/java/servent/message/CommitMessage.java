package servent.message;

import file.GitFile;
import lombok.Getter;

import java.io.File;

@Getter
public class CommitMessage extends BasicMessage{


    private static final long serialVersionUID = -2144883641345474574L;
    private int target;
    private GitFile gitFile;

    public CommitMessage(int senderPort, int receiverPort, GitFile gitFile, int target) {
        super(MessageType.COMMIT, senderPort, receiverPort);
        this.gitFile=gitFile;
        this.target=target;
    }

}
