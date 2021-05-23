package servent.message.CRUD;

import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;
@Getter
public class AskPullMessage extends BasicMessage {


    private static final long serialVersionUID = -3746412815617942221L;

    private int target;
    public AskPullMessage(int senderPort, int receiverPort, String text,int target) {
        super(MessageType.ASK_PULL, senderPort, receiverPort, text);
        this.target=target;
    }

    @Override
    public String toString() {
        return "AskPullMessage{" +
                "target=" + target +
                '}';
    }
}