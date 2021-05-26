package servent.message.TEAM_ORG;

import file.GitFile;
import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;



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

    @Override
    public String toString() {
        return "MessageInfoTeam: " + super.getSenderPort()+" | "+super.getReceiverPort()+" | "+super.getMessageId()+" | "+gitFile+" | "+ idKodKogaJe+" | "+ target;
    }

}
