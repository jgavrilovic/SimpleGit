package servent.handler.FAILURE;

import app.AppConfig;
import servent.handler.MessageHandler;
import servent.message.FAILURE.PingMessage;
import servent.message.FAILURE.PingMessage1;
import servent.message.FAILURE.PongMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.util.Timer;
import java.util.TimerTask;


public class PingHandler implements MessageHandler {

    private Message clientMessage;

    public PingHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    /**
     * -veze u sistemu
     * -cuvanje podataka(kloniranje)
     * -raspordjivanje datoteka
     * -otkaz cvora i reorganizacija

     * */

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() { counter++; }
    };
    private Timer timer = new Timer("MyTimer");
    private int counter=0;
    private boolean send = true;
    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.PING) {
            try {
                if(clientMessage.getMessageText().equals("")){
                    Message pong = new PongMessage(AppConfig.myServentInfo.getListenerPort(),clientMessage.getSenderPort(),"");
                    MessageUtil.sendMessage(pong);


                    Thread.sleep(5000);
                    Message pingNext = new PingMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodePort(),"");
                    MessageUtil.sendMessage(pingNext);
                    timer.scheduleAtFixedRate(timerTask, 0, 1000);

                    AppConfig.timestampedErrorPrint(counter+"");
                    while (true){
                        AppConfig.timestampedErrorPrint(counter+"");
                        if(counter<4){
                            if(PongHandler.basicPongRecived){
                                AppConfig.timestampedErrorPrint("<4 prva");
                                PongHandler.basicPongRecived=false;
                                counter=0;
                                timer.cancel();
                                break;
                            }
                        }else if(counter==4){
                            if(PongHandler.basicPongRecived){
                                AppConfig.timestampedErrorPrint("=4 prva");
                                PongHandler.basicPongRecived=false;
                                counter=0;
                                timer.cancel();
                                break;
                            }else {
                                if(send){
                                    AppConfig.timestampedErrorPrint("ALERT");
                                    Message pingAlert = new PingMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getPredecessor().getListenerPort(),"ALERT");
                                    MessageUtil.sendMessage(pingAlert);
                                    send=false;
                                }
                            }
                        }else if(counter>4){
                            AppConfig.timestampedErrorPrint(">4 prva");
                            send=true;
                            if(PongHandler.basicPongRecived){
                                PongHandler.basicPongRecived=false;
                                counter=0;
                                timer.cancel();
                                break;
                            }else{
                                if(PongHandler.confirm){
                                    //brisi sumnjiv node
                                    PongHandler.confirm=false;
                                    counter=0;
                                    timer.cancel();
                                    break;
                                }
                            }
                        }
                        Thread.sleep(500);
                    }

                }else if(clientMessage.getMessageText().equals("ALERT")){
                    AppConfig.timestampedErrorPrint("DOBIO ALERT");

                    AppConfig.timestampedErrorPrint(AppConfig.chordState.getSuccessorTable()[0]+"");
                    AppConfig.timestampedErrorPrint(AppConfig.chordState.getSuccessorTable()[1]+"");
                    AppConfig.timestampedErrorPrint(AppConfig.chordState.getSuccessorTable()[2]+"");
                    AppConfig.timestampedErrorPrint(AppConfig.chordState.getSuccessorTable()[3]+"");
                    AppConfig.timestampedErrorPrint(AppConfig.chordState.getSuccessorTable()[4]+"");
                    AppConfig.timestampedErrorPrint(AppConfig.chordState.getSuccessorTable()[5]+"");
                    Message pingAlert = new PingMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getSuccessorTable()[5].getListenerPort(),"BROKEN?");
                    MessageUtil.sendMessage(pingAlert);
                    timer.scheduleAtFixedRate(timerTask, 0, 1000);


                    while (true){
                        if(counter<4){
                            AppConfig.timestampedErrorPrint("<4 druga");
                            if(PongHandler.alertPongRecived){
                                PongHandler.alertPongRecived=false;
                                counter=0;
                                timer.cancel();
                                break;
                            }
                        }else {
                            AppConfig.timestampedErrorPrint(">=4 druga");
                            if(PongHandler.alertPongRecived){
                                PongHandler.alertPongRecived=false;
                                counter=0;
                                timer.cancel();
                                break;
                            }else{
                                if(counter>10){
                                    Message pongConfirm = new PongMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodePort(),"CONFIRM");
                                    MessageUtil.sendMessage(pongConfirm);
                                    counter=0;
                                    timer.cancel();
                                    break;
                                }
                            }
                        }
                        Thread.sleep(500);
                    }

                }else if(clientMessage.getMessageText().equals("BROKEN?")){
                    Message pongNO = new PongMessage(AppConfig.myServentInfo.getListenerPort(),clientMessage.getSenderPort(),"I AM NOT BROKEN");
                    MessageUtil.sendMessage(pongNO);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            AppConfig.timestampedErrorPrint("PING handler got a message that is not PING");
        }

    }
}
