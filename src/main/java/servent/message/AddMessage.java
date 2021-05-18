package servent.message;


import java.io.File;
import java.nio.file.Path;

public class AddMessage extends BasicMessage{


    private static final long serialVersionUID = 8579457735906956418L;
    private String path;
    private int target;

    private File file;

    public AddMessage(int senderPort, int receiverPort, File file, int target) {
        super(MessageType.ADD_GITFILE, senderPort, receiverPort);

        this.file=file;
        this.target=target;
    }


    public int getTarget() {
        return target;
    }

    public File getFile() {
        return file;
    }
}
