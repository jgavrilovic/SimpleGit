package servent.message.CRUD;

import file.GitFile;
import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;

@Getter
public class UpdateDHTMessage extends BasicMessage {


    private static final long serialVersionUID = 4430898306487087029L;
    private int target;
    private GitFile gitFile;

    public UpdateDHTMessage(int senderPort, int receiverPort, GitFile gitFile, int target) {
        super(MessageType.UPDATEDHT, senderPort, receiverPort);
        this.gitFile=gitFile;
        this.target=target;
    }

}
