package servent.handler.FAILURE;

import app.AppConfig;
import servent.handler.MessageHandler;
import servent.message.*;
import servent.message.FAILURE.*;
import servent.message.util.MessageUtil;

public class PingHandler1 implements MessageHandler {

    private Message clientMessage;
    public PingHandler1(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    /**
     * Uz svaku poruku koju saljem ide logika za potvrdu da je cvor ili medjucvor prihvatio(ako se bane slozi)
     * Ako potvrda ne stigne , kroz logiku znacu gde je problem i problem resavam po zahteviam specifikacije,
     * tako sto cu da kazem random cvoru da ga pinga..
     *
     *
     * */


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.PING) {
            PingMessage1 message = (PingMessage1)clientMessage;
            try {
                switch (message.getPingType()){
                    case PING:
                        // Ako sam konacna destinacija Pinga
                        if (message.getOriginalReciverID() == AppConfig.myServentInfo.getChordId()){

                            PongMessage1 pongMessage1 = new PongMessage1(AppConfig.myServentInfo.getListenerPort(),
                                    AppConfig.chordState.getNextNodeForKey(message.getOriginalSenderID()).getListenerPort(),
                                    PongType.PONG, message.getMessageId());

                            MessageUtil.sendMessage(pongMessage1);
                        }
                        else{
                            //prosledjujemo ping
                            PingMessage1 pingMessage = new PingMessage1(AppConfig.myServentInfo.getListenerPort(),
                                    AppConfig.chordState.getNextNodeForKey(message.getOriginalReciverID()).getListenerPort(),
                                    message.getOriginalSenderID(), message.getOriginalReciverID(),
                                    PingType.PING);

                            MessageUtil.sendMessage(pingMessage);


                        }
                        //saljemo potvrdu poslednjem ko je slao
                        MessageUtil.sendDeliveredMessage(message);

                        break;
                    case CHECK_REQUEST:
                        //handle checkRequest
                        break;
                    case CHECK:
                        //handleCheck
                        break;
                    default:
                        AppConfig.timestampedErrorPrint("Neocekivan tip poruke stigao na PingHandler1");
                        break;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            AppConfig.timestampedErrorPrint("Ping handler got a message that is not Ping");
        }

    }
}
