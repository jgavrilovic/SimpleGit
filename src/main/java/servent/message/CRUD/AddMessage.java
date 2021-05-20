package servent.message.CRUD;


import servent.message.BasicMessage;
import servent.message.MessageType;

import java.io.File;
import java.nio.file.Path;

public class AddMessage extends BasicMessage {


    private static final long serialVersionUID = 8579457735906956418L;
    private String path;



    private int sender;
    private int target;

    private File file;

    public AddMessage(int senderPort, int receiverPort, File file, int sender, int target) {
        super(MessageType.ADD_GITFILE, senderPort, receiverPort);

        this.file=file;
        this.sender=sender;
        this.target=target;
    }
    public int getSender() {
        return sender;
    }

    public int getTarget() {
        return target;
    }

    public File getFile() {
        return file;
    }
}
