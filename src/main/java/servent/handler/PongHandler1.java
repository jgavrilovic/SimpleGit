package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.PingMessage;
import servent.message.PongMessage;
import servent.message.util.MessageUtil;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PongHandler1 implements MessageHandler {

    private Message clientMessage;
    public PongHandler1(Message clientMessage) {
        this.clientMessage = clientMessage;
    }




    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.PONG) {

            PongMessage message = (PongMessage)clientMessage;
            try {

                switch (message.getPongType()){
                    case PONG:
                        AppConfig.timestampedErrorPrint("Primio PONG");
                        MessageUtil.sendDeliveredMessage(message);
                        break;
                    case CHECK_FAIL:
                        //handle CHECK_FAIL
                        break;
                    case CHECK_SUCCESS:
                        //CHECK_SUCCESS
                        break;
                    case DELIVERED:
                        //Iskljuci timeout brojac (NAPISI LOGIKU ZA POKRETANJE TIMEOUT BROJACA IZ MESSAGE SENDERA)
                        AppConfig.timestampedErrorPrint("Poruka: " + message.getPreviousMessageID() + " je prihvacena");
                        MessageUtil.deliverMessageByID(message.getPreviousMessageID());
                        break;
                    default:
                        //Ispisi error
                        AppConfig.timestampedErrorPrint("Neocekivan tip poruke stigao na PongHandler1");
                        break;
                }



            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            AppConfig.timestampedErrorPrint("PONG handler got a message that is not PONG");
        }

    }
}
