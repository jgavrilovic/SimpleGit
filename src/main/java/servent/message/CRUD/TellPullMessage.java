package servent.message.CRUD;

import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;


@Getter
public class TellPullMessage  extends BasicMessage {


    private static final long serialVersionUID = 1005997322294612755L;

    private String nameOfFile;
    private String contentOfFile;
    private int version;

    public TellPullMessage(int senderPort, int receiverPort, String nameOfFile, String contentOfFile,int version) {
        super(MessageType.TELL_PULL, senderPort, receiverPort);
        this.nameOfFile=nameOfFile;
        this.contentOfFile=contentOfFile;
        this.version=version;
    }

}
