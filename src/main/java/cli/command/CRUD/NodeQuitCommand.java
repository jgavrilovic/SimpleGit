package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import servent.handler.START.NodeQuitHandler;
import servent.message.Message;
import servent.message.START.NodeQuitMessage;
import servent.message.util.MessageUtil;

public class NodeQuitCommand implements CLICommand {

    @Override
    public String commandName() {
        return "quit";
    }

    @Override
    public void execute(String args) {
        try {

            /**TODO
             *  Kada su dva cvora baca error*/
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

