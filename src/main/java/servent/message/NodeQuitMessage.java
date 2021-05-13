package servent.message;

import app.ServentInfo;

import java.util.Map;

public class NodeQuitMessage extends BasicMessage {


    private static final long serialVersionUID = -2457521687571225150L;


    public NodeQuitMessage(int senderPort, int receiverPort, String text, Map<Integer, Integer> valueMap,int originalSenderID,int originalReciverID) {
        super(MessageType.QUIT, senderPort, receiverPort, text, valueMap,originalSenderID,originalReciverID);

    }

}
