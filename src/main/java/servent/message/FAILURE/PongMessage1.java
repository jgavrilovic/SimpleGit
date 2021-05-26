package servent.message.FAILURE;

import servent.message.BasicMessage;
import servent.message.MessageType;

public class PongMessage1 extends BasicMessage {

    private static final long serialVersionUID = -5614361189961750701L;

    private PongType pongType;
    private int previousMessageID;



    public PongMessage1(int senderPort, int receiverPort, PongType type, int previousMessageID) {
        super(MessageType.PONG, senderPort, receiverPort);
        this.pongType = type;
        this.previousMessageID = previousMessageID;
    }

    public PongType getPongType() {
        return pongType;
    }

    public void setPongType(PongType pongType) {
        this.pongType = pongType;
    }

    public int getPreviousMessageID() {
        return previousMessageID;
    }
}

