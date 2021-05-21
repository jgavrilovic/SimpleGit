package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import servent.handler.CRUD.ConflictHandler;
import servent.message.CRUD.ConflictMessage;
import servent.message.CRUD.ConflictType;
import servent.message.util.MessageUtil;

public class PushConflictCommand implements CLICommand {

    /**
     *
     * posaljem @ConflictMessage biraj opcije, null
     * vrati mi view - posaljem komanda view, gitgile()
     * vrati mi push - posaljem DONE_PUSH, null ->upisem njegovu
     * vrati mi pull - posaljem DONE_Pull, gitfile() -> update
     *
     * */

    @Override
    public String commandName() {
        return "push-conflict";
    }


    @Override
    public void execute(String args) {
        ConflictMessage conflictMessage = new ConflictMessage(
                AppConfig.myServentInfo.getListenerPort(),
                AppConfig.chordState.getNextNodeForKey(ConflictHandler.fromTO).getListenerPort(),
                ConflictType.DONE_PUSH,
                null,
                AppConfig.myServentInfo.getChordId(),
                ConflictHandler.fromTO);
        MessageUtil.sendMessage(conflictMessage);
    }
}