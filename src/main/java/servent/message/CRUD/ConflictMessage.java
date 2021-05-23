package servent.message.CRUD;

import file.GitFile;
import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;

@Getter
public class ConflictMessage extends BasicMessage {


    private static final long serialVersionUID = -7368203647999743745L;
    private ConflictType conflictType;

    private int senderID;
    private int target;

    private String name;
    private String content;
    private int version;

    public ConflictMessage(int senderPort, int receiverPort,  ConflictType conflictType,  int senderID, int target) {
        super(MessageType.CONFLICT, senderPort, receiverPort);
        this.conflictType=conflictType;
        this.senderID=senderID;
        this.target=target;

        this.name="";
        this.content="";
        this.version=0;
    }

    public ConflictMessage(int senderPort, int receiverPort,  String name, String content, ConflictType conflictType, int senderID, int target) {
        super(MessageType.CONFLICT, senderPort, receiverPort);
        this.name=name;
        this.content=content;
        this.conflictType=conflictType;
        this.senderID=senderID;
        this.target=target;

        this.version=0;
    }

    public ConflictMessage(int senderPort, int receiverPort, String name, String content, int version, ConflictType conflictType, int senderID, int target) {
        super(MessageType.CONFLICT, senderPort, receiverPort);
        this.name=name;
        this.content=content;
        this.version=version;
        this.conflictType=conflictType;
        this.senderID=senderID;
        this.target=target;
    }

    @Override
    public String toString() {
        return "ConflictMessage{" +
                "conflictType=" + conflictType +
                ", senderID=" + senderID +
                ", target=" + target +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", version=" + version +
                '}';
    }
}
