package servent.message.CRUD;

import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;

@Getter
public class CommitMessage extends BasicMessage {


    private static final long serialVersionUID = -2144883641345474574L;
    private int senderID;
    private int target;
    private String nameOfFile;
    private String contentOfFile;
    private int version;


    public CommitMessage(int senderPort, int receiverPort, String nameOfFile,String contentOfFile,int version, int senderID ,int target) {
        super(MessageType.COMMIT, senderPort, receiverPort);
        this.nameOfFile=nameOfFile;
        this.contentOfFile=contentOfFile;
        this.version=version;
        this.senderID=senderID;
        this.target=target;
    }

    @Override
    public String toString() {
        return "CommitMessage{" +
                "senderID=" + senderID +
                ", target=" + target +
                ", nameOfFile='" + nameOfFile + '\'' +
                ", contentOfFile='" + contentOfFile + '\'' +
                ", version=" + version +
                '}';
    }
}
