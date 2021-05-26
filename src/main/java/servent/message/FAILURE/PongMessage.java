package servent.message.FAILURE;

import servent.message.BasicMessage;
import servent.message.MessageType;

public class PongMessage extends BasicMessage {

    private static final long serialVersionUID = -5614361189961750701L;


    public PongMessage(int senderPort, int receiverPort, String text) {
        super(MessageType.PONG, senderPort, receiverPort,text);
    }

    @Override
    public String toString() {
        return "PongMessage: " + super.getSenderPort()+" | "+super.getReceiverPort()+" | "+super.getMessageId()+" | "+super.getMessageText();
    }
}

