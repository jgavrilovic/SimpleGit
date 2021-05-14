package cli.command;

import app.AppConfig;
import servent.handler.NodeQuitHandler;
import servent.message.Message;
import servent.message.NodeQuitMessage;
import servent.message.UpdateMessage;
import servent.message.util.MessageUtil;

import java.util.Arrays;
import java.util.HashMap;


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

            Message nqtMsg = new NodeQuitMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePort(), msg, AppConfig.chordState.getValueMap(),
                    AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodePort());
            MessageUtil.sendMessage(nqtMsg);
            NodeQuitHandler.receivedQuit.add(nqtMsg);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}

