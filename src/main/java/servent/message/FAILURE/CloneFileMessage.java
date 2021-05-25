package servent.message.FAILURE;

import file.GitFile;
import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;
@Getter
public class CloneFileMessage extends BasicMessage {


    private static final long serialVersionUID = 8579457735906956418L;
    private String nameOfFile;
    private String contentOfFile;
    private GitFile gitFile;
    private int sender;
    private int target;

    public CloneFileMessage(int senderPort, int receiverPort, String nameOfFile, String contentOfFile,GitFile gitFile, int sender, int target) {
        super(MessageType.CLONE_GITFILE, senderPort, receiverPort);
        this.nameOfFile=nameOfFile;
        this.contentOfFile=contentOfFile;
        this.gitFile=gitFile;
        this.sender=sender;
        this.target=target;
    }

    @Override
    public String toString() {
        return "CloneFileMessage{" +
                "nameOfFile='" + nameOfFile + '\'' +
                ", contentOfFile='" + contentOfFile + '\'' +
                ", gitFile=" + gitFile +
                ", sender=" + sender +
                ", target=" + target +
                '}';
    }
}
