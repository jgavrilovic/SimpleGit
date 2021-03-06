package cli.command.FAILURE;


import app.AppConfig;
import cli.command.CLICommand;
import servent.message.FAILURE.PingMessage;
import servent.message.FAILURE.PingMessage1;
import servent.message.FAILURE.PingType;
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
                AppConfig.timestampedErrorPrint("Unesi cvor kao target");
            }else{
                key = Integer.parseInt(args);
            }


            PingMessage pingMessage = new PingMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodePort(), "");
            MessageUtil.sendMessage(pingMessage);


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}