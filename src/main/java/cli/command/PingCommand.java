package cli.command;


import app.AppConfig;
import servent.message.PingMessage;
import servent.message.PingType;
import servent.message.util.MessageUtil;

public class PingCommand implements CLICommand {


    private int counter = 0 ;

    @Override
    public String commandName() {
        return "ping";
    }


    int key=0;
    @Override
    public void execute(String args) {
        try {
            if(args==null){
                AppConfig.timestampedErrorPrint("Unesi cvor nako target");
            }else{
                key = Integer.parseInt(args);
            }


            PingMessage pingMessage = new PingMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(key).getListenerPort(), AppConfig.myServentInfo.getChordId(), key, PingType.PING);
            MessageUtil.sendMessage(pingMessage);


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}