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

        Pattern p = Pattern.compile("[0-9]\\.");
        String path = ConflictHandler.fileProblem.substring(ConflictHandler.fileProblem.indexOf("localStorage")+13); //src\main\resources\servent1\localStorage\dir1\b0.txt
        AppConfig.timestampedErrorPrint(path);
        Matcher m = p.matcher(path);
        m.find();
        int version=0;
        try{
            AppConfig.timestampedErrorPrint(m.group());
             version = Integer.parseInt(m.group().replace(".",""));
        }catch (IllegalStateException e){
            AppConfig.timestampedErrorPrint("Doslo je do greske pri obradi verzije");
        } catch (NumberFormatException e){
            AppConfig.timestampedErrorPrint("Doslo je do greske , pri transformisanju string->int");
        }

        String content = AddCommand.fileReader(AppConfig.myServentInfo.getRootPath()+"/"+path);
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