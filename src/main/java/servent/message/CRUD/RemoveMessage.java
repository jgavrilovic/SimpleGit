package servent.message.CRUD;

import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;
@Getter
public class RemoveMessage extends BasicMessage {


    private static final long serialVersionUID = -6106208014707411216L;
    private int senderID;
    private int target;

    public RemoveMessage(int senderPort, int receiverPort, String messageText, int senderID, int target) {
        super(MessageType.DELETE, senderPort, receiverPort,messageText);

        this.senderID=senderID;
        this.target=target;
    }

}
