package servent.message;

import java.util.Map;

public class WelcomeMessage extends BasicMessage {

	private static final long serialVersionUID = -8981406250652693908L;

	private Map<Integer, Integer> values;
	
	public WelcomeMessage(int senderPort, int receiverPort, Map<Integer, Integer> values,int originalSenderID,int originalReciverID) {
		super(MessageType.WELCOME, senderPort, receiverPort,originalSenderID,originalReciverID);
		
		this.values = values;
	}
	
	public Map<Integer, Integer> getValues() {
		return values;
	}
}
