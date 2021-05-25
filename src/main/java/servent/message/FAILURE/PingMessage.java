package servent.message.FAILURE;

import servent.message.BasicMessage;
import servent.message.MessageType;

public class PingMessage extends BasicMessage {

    private static final long serialVersionUID = 6816219166038652857L;


    public PingMessage(int senderPort, int receiverPort, String text) {
        super(MessageType.PING, senderPort, receiverPort,text);

    }



}
