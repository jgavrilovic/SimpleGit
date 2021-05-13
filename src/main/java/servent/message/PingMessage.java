package servent.message;

public class PingMessage  extends BasicMessage {

    private static final long serialVersionUID = 6816219166038652857L;

    private PingType pingType;

    public PingMessage(int senderPort, int receiverPort, int originalSenderID, int originalReciverID, PingType type) {
        super(MessageType.PING, senderPort, receiverPort, originalSenderID, originalReciverID);
        this.pingType = type;
    }

    public PingType getPingType() {
        return pingType;
    }

    public void setPingType(PingType pingType) {
        this.pingType = pingType;
    }


}
