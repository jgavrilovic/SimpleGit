package servent.message.START;

import app.ServentInfo;
import servent.message.BasicMessage;
import servent.message.MessageType;

import java.util.Map;

public class NodeQuitMessage extends BasicMessage {


    private static final long serialVersionUID = -2457521687571225150L;


    public NodeQuitMessage(int senderPort, int receiverPort, String text, Map<Integer, Integer> valueMap) {
        super(MessageType.QUIT, senderPort, receiverPort, text, valueMap);

    }

}
