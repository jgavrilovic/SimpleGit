package servent.message;

import file.GitFile;

import java.io.File;

public class TellPullMessage  extends BasicMessage {


    private static final long serialVersionUID = 1005997322294612755L;
    private GitFile file;
    public TellPullMessage(int senderPort, int receiverPort, GitFile fIle) {
        super(MessageType.TELL_PULL, senderPort, receiverPort);
        this.file=fIle;
    }


    public GitFile getFile() {
        return file;
    }
}
