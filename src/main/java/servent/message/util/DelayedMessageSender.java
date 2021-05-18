package servent.message.util;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.PongMessage;
import servent.message.PongType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This worker sends a message asynchronously. Doing this in a separate thread
 * has the added benefit of being able to delay without blocking main or somesuch.
 * 
 * @author bmilojkovic
 *
 */
public class DelayedMessageSender implements Runnable {

	private Message messageToSend;
	
	public DelayedMessageSender(Message messageToSend) {
		this.messageToSend = messageToSend;
	}
	
	public void run() {
		/*
		 * A random sleep before sending.
		 * It is important to take regular naps for health reasons.
		 */
		try {
			Thread.sleep((long)(Math.random() * 1000) + 500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		if (MessageUtil.MESSAGE_UTIL_PRINTING) {
			AppConfig.timestampedStandardPrint("Sending message " + messageToSend);
		}
		
		try {
			Socket sendSocket = new Socket(messageToSend.getReceiverIpAddress(), messageToSend.getReceiverPort());
			
			ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
			oos.writeObject(messageToSend);
			oos.flush();
			
			sendSocket.close();
/*
			if (messageToSend.getMessageType() != MessageType.PONG){
				Thread thread = new Thread(new TimeoutCounter(messageToSend));
				thread.start();

			}else {
				if (((PongMessage)messageToSend).getPongType() != PongType.DELIVERED){
					Thread thread = new Thread(new TimeoutCounter(messageToSend));
					thread.start();
				}
			}*/
		} catch (IOException e) {
			e.printStackTrace();
			AppConfig.timestampedErrorPrint("Couldn't send message: " + messageToSend.toString());
		}
	}
	
}
