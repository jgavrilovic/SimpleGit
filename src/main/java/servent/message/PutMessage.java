package servent.message;

public class PutMessage extends BasicMessage {

	private static final long serialVersionUID = 5163039209888734276L;

	public PutMessage(int senderPort, int receiverPort, int key, int value,int originalSenderID,int originalReciverID) {
		super(MessageType.PUT, senderPort, receiverPort, key + ":" + value,null,originalSenderID,originalReciverID);
	}
}
