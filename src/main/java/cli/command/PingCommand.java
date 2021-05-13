package cli.command;


import app.AppConfig;
import app.ServentInfo;
import servent.handler.PongHandler;
import servent.message.PingMessage;
import servent.message.PingType;
import servent.message.PongMessage;
import servent.message.util.MessageUtil;

import java.util.Timer;
import java.util.TimerTask;

public class PingCommand implements CLICommand {


    private int counter = 0 ;

    @Override
    public String commandName() {
        return "ping";
    }



    @Override
    public void execute(String args) {
        try {
            int key = Integer.parseInt(args);

            PingMessage pingMessage = new PingMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(key).getListenerPort(), AppConfig.myServentInfo.getChordId(), key, PingType.PING);
            MessageUtil.sendMessage(pingMessage);


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}