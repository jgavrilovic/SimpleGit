package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import servent.handler.CRUD.ConflictHandler;
import servent.message.CRUD.ConflictMessage;
import servent.message.CRUD.ConflictType;
import servent.message.util.MessageUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PushConflictCommand implements CLICommand {

    @Override
    public String commandName() {
        return "push-conflict";
    }


    @Override
    public void execute(String args) {

        AppConfig.timestampedErrorPrint(ConflictHandler.fileProblem);
        Pattern p = Pattern.compile("[0-9]");
        String path = ConflictHandler.fileProblem.substring(ConflictHandler.fileProblem.indexOf("localRoot")+10);
        Matcher m = p.matcher(path);
        m.find();
        int a = Integer.parseInt(m.group());

        String content = AddCommand.fileReader(AppConfig.myServentInfo.getRootPath()+"/"+path.replaceAll("[0-9]",a-1+""));
        AppConfig.timestampedErrorPrint("PUSH: " + content);
        ConflictMessage conflictMessage = new ConflictMessage(
                AppConfig.myServentInfo.getListenerPort(),
                AppConfig.chordState.getNextNodeForKey(ConflictHandler.target).getListenerPort(),
                ConflictHandler.fileProblem,
                content,
                ConflictType.PUSH,
                AppConfig.myServentInfo.getChordId(),
                ConflictHandler.target);
        MessageUtil.sendMessage(conflictMessage);
    }
}