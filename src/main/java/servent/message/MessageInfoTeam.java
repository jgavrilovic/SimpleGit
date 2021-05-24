package servent.message;

import file.GitFile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;

@Getter
public class MessageInfoTeam extends BasicMessage {


    private static final long serialVersionUID = -393955127531436147L;

    private GitFile gitFile;
    private int idKodKogaJe;
    private int target;
    public MessageInfoTeam(int senderPort, int receiverPort,  GitFile gitFile,int target,int idKodKogaJe) {
        super(MessageType.TEAM_INFO, senderPort, receiverPort);
        this.gitFile=gitFile;
        this.target=target;
        this.idKodKogaJe=idKodKogaJe;
    }

}
