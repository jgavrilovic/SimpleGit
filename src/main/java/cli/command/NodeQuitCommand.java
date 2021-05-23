package cli.command;

import app.AppConfig;
import servent.handler.START.NodeQuitHandler;
import servent.message.Message;
import servent.message.START.NodeQuitMessage;
import servent.message.util.MessageUtil;


/*
    -Saljem poruku da zelim da se iskljucim iz sistema
*/

public class NodeQuitCommand implements CLICommand {

    @Override
    public String commandName() {
        return "quit";
    }

    @Override
    public void execute(String args) {
        try {

            //port - id - port(mog prethodnika)
            String msg = AppConfig.myServentInfo.getListenerPort() + " " + AppConfig.myServentInfo.getChordId() +" " + AppConfig.chordState.getPredecessor().getListenerPort();

            Message nqtMsg = new NodeQuitMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePort(), msg, AppConfig.chordState.getValueMap());
            MessageUtil.sendMessage(nqtMsg);
            NodeQuitHandler.receivedQuit.add(nqtMsg);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}

