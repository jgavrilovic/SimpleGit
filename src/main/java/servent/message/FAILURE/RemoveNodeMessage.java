package servent.message.FAILURE;


import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;
@Getter
public class RemoveNodeMessage extends BasicMessage {


    private static final long serialVersionUID = 2993267201714892741L;
    private int removeID;
    private int target;
    public RemoveNodeMessage(int senderPort, int receiverPort, int removeID, int target) {
        super(MessageType.REMOVE_NODE, senderPort, receiverPort);
        this.removeID=removeID;
        this.target=target;
    }

    @Override
    public String toString() {
        return "RemoveNodeMessage: " + super.getSenderPort()+" | "+super.getReceiverPort()+" | "+super.getMessageId()+" | "+removeID+" | "+ target;
    }
}
