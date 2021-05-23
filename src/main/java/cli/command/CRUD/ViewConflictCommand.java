package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import servent.handler.CRUD.ConflictHandler;
import servent.message.CRUD.ConflictMessage;
import servent.message.CRUD.ConflictType;
import servent.message.util.MessageUtil;

public class ViewConflictCommand implements CLICommand {



    @Override
    public String commandName() {
        return "view-conflict";
    }


    @Override
    public void execute(String args) {
        ConflictMessage conflictMessage = new ConflictMessage(
                AppConfig.myServentInfo.getListenerPort(),
                AppConfig.chordState.getNextNodeForKey(ConflictHandler.target).getListenerPort(),
                ConflictType.VIEW,
                AppConfig.myServentInfo.getChordId(),
                ConflictHandler.target);
        MessageUtil.sendMessage(conflictMessage);
    }
}