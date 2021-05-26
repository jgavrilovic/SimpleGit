package servent.message.CRUD;


import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;

import java.io.File;
import java.nio.file.Path;

@Getter
public class AddMessage extends BasicMessage {


    private static final long serialVersionUID = 8579457735906956418L;
    private int sender;
    private int target;
    private String nameOfFile;
    private String contentOfFile;
    public AddMessage(int senderPort, int receiverPort, String nameOfFile, String contentOfFile, int sender, int target) {
        super(MessageType.ADD_GITFILE, senderPort, receiverPort);
        this.nameOfFile=nameOfFile;
        this.contentOfFile=contentOfFile;
        this.sender=sender;
        this.target=target;
    }

    @Override
    public String toString() {
        return "AskPullMessage: "
                + super.getSenderPort()
                + " | " + super.getReceiverPort()
                + " | " + super.getMessageId()
                + " | " + nameOfFile
                + " | " + contentOfFile
                + " | " + target;
    }
}
