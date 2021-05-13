package servent.message;

public class PoisonMessage extends BasicMessage {

	private static final long serialVersionUID = -5625132784318034900L;

	public PoisonMessage(int originalSenderID,int originalReciverID) {
		super(MessageType.POISON, 0, 0,originalSenderID,originalReciverID);
	}
}
