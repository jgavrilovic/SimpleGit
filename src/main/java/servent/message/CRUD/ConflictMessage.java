package servent.message.CRUD;

import file.GitFile;
import lombok.Getter;
import servent.message.BasicMessage;
import servent.message.MessageType;

@Getter
public class ConflictMessage extends BasicMessage {

    //view - Dohvatanje datoteke iz sistema pod privremenim nazivom, kako bi se ispitao njen sadržaj. Nakon ove operacije se korisnik ponovo pita za opciju razrešavanja konflikta.
    //push - Prepisivanje datoteke u sistemu sa lokalnom datotekom.
    //pull - Prepisivanje lokalne datoteke sa datotekom iz sistema.

    private static final long serialVersionUID = -2144883641345474574L;
    private ConflictType conflictType;
    private GitFile gitFile;
    private int senderID;
    private int target;


    public ConflictMessage(int senderPort, int receiverPort, ConflictType conflictType, GitFile gitFile, int senderID, int target) {
        super(MessageType.CONFLICT, senderPort, receiverPort);
        this.conflictType=conflictType;
        this.senderID=senderID;
        this.gitFile=gitFile;
        this.target=target;
    }

    /**
     *
     * posaljem @ConflictMessage biraj opcije, null
     * vrati mi view - posaljem komanda view, gitgile()
     * vrati mi push - posaljem DONE_PUSH, null ->upisem njegovu
     * vrati mi pull - posaljem DONE_Pull, gitfile() -> update
     *
     * */

}
