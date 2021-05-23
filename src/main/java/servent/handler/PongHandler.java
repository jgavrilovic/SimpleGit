package servent.handler;

import servent.message.Message;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PongHandler implements MessageHandler {

    public static Set<Message> receivedPong = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Message clientMessage;

    public PongHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

    }
}

//successor_info

/**
 * ja sam 0: saljem PING
 *
 *
 * Stigao mi PING: (ja sam 1)
 *      Saljem cvoru iza PONG
 *      Saljem cvoru ispred PING
 *      Krece timer za odgovor sa njegovim IDem
 *          cekam do 1000ms:
 *              DOBIO->prekidam timer
 *              NE->cekam da predje 1000ms
 *      Preslo je 1000ms saljem cvoru iza da ga PING-a i cekam odgovor od njega
 *          Da->cekam 10000ms i saljem da ga uklone svi
 *          Ne->sa mnom nesto nije uredu JEL?!
 *
 *
 * Stigao mi PONG:  (ja sam 0)
 *        prazna poruka je od 1..sve cool
 *        alert poruka od 1.. daj ja da pingam 2
 *        no poruka od 2.. dakle sa kecom nesto nije ok
 *
 * */
/*
    public static boolean basicPongRecived = false;
    public static boolean alertPongRecived = false;
    public static boolean confirm = false;
    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.PONG) {
            try {

                if(clientMessage.getMessageText().equals("")){
                    AppConfig.timestampedErrorPrint("OK");
                    basicPongRecived=true;
                } else if(clientMessage.getMessageText().equals("I AM NOT BROKEN")){
                    AppConfig.timestampedErrorPrint("I AM NOT BROKEN");
                    alertPongRecived=true;
                    Message pongWtf = new PongMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodePort(),"YOU ARE PROBLEM");
                    MessageUtil.sendMessage(pongWtf);
                } else if(clientMessage.getMessageText().equals("YOU ARE PROBLEM")){

                    AppConfig.timestampedErrorPrint("NZM U CEMU JE PROBLEM ONDA??");
                } else if(clientMessage.getMessageText().equals("CONFIRM")) {
                    AppConfig.timestampedErrorPrint("OBRISI GA");
                    confirm =true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            AppConfig.timestampedErrorPrint("PONG handler got a message that is not PONG");
        }

    }
}
*/

