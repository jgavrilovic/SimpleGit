package servent.message.util;

import app.AppConfig;
import servent.message.Message;

import java.util.Timer;
import java.util.TimerTask;

public class TimeoutCounter implements Runnable {

    Message message;
    private boolean SUS_CHECK_DONE = false;

    private int counter=0;
    TimeoutCounter(Message message){
        this.message = message;
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            counter++;
        }
    };
    Timer timer = new Timer("MyTimer");


    @Override
    public void run() {

        MessageUtil.addMessageToMap(message);

         timer.scheduleAtFixedRate(timerTask,0,100);

         while (counter<100){
             if(counter>10 & !SUS_CHECK_DONE){
                 //cvor kome je poslato je sumnjiv, ovde logika za sumnjivog
                 AppConfig.timestampedErrorPrint("CVOR KOME JE POSLATO JE SUMNJIV " + message.getMessageId());
                 SUS_CHECK_DONE = true;
             }
            if(MessageUtil.isDeliveredByID(message.getMessageId())){
                timer.cancel();
                break;
            }
         }
         MessageUtil.removeDeliveredMessageByID(message.getMessageId());
         if (counter >= 100){
             //ne odgovara, oso u kurac
             AppConfig.timestampedErrorPrint("PROBLEM U ODGOVORU NA PORUKU " + message.getMessageId());
         } else {
             //sve je OK
             AppConfig.timestampedErrorPrint("SVE JE OK SA PORUKOM " + message.getMessageId() + ", counter:"+ counter);
         }
         counter = 0;
    }
}
