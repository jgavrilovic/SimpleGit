package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import file.HashFile;
import servent.message.CRUD.RemoveMessage;
import servent.message.util.MessageUtil;

public class RemoveCommand  implements CLICommand {

    @Override
    public String commandName() {
        return "remove";
    }

    @Override
    public void execute(String args) {
        //izvucem iz argumenta ime trazene datoteke file/dir
        //hashiram po punoj putanji
        String fullpath1= args.replaceAll("\\d\\.",".");
        AppConfig.timestampedStandardPrint(fullpath1);
        int destination = HashFile.hashFileName(fullpath1);

        AppConfig.timestampedErrorPrint("Izabran cvor: " + destination);
        RemoveMessage removeMessage = new RemoveMessage(
                AppConfig.myServentInfo.getListenerPort(),
                AppConfig.chordState.getNextNodeForKey(destination).getListenerPort(),
                args,
                AppConfig.myServentInfo.getChordId(),
                destination);
        MessageUtil.sendMessage(removeMessage);


    }
}