package servent.message.FAILURE;

import servent.message.BasicMessage;
import servent.message.MessageType;

public class PingMessage  extends BasicMessage {

    private static final long serialVersionUID = 6816219166038652857L;

    private PingType pingType;


    private int originalSenderID;
    private int originalReciverID;

    public PingMessage(int senderPort, int receiverPort, int originalSenderID, int originalReciverID, PingType type) {
        super(MessageType.PING, senderPort, receiverPort);
        this.pingType = type;
        this.originalSenderID=originalSenderID;
        this.originalReciverID=originalReciverID;
    }

    public PingType getPingType() {
        return pingType;
    }

    public void setPingType(PingType pingType) {
        this.pingType = pingType;
    }

    public int getOriginalSenderID() {
        return originalSenderID;
    }

    public int getOriginalReciverID() {
        return originalReciverID;
    }


}
