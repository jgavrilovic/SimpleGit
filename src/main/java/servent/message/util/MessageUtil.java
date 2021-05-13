package servent.message.util;

import app.AppConfig;
import servent.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * For now, just the read and send implementation, based on Java serializing.
 * Not too smart. Doesn't even check the neighbor list, so it actually allows cheating.
 * 
 * Depending on the configuration it delegates sending either to a {@link DelayedMessageSender}
 * in a new thread (non-FIFO) or stores the message in a queue for the {@link FifoSendWorker} (FIFO).
 * 
 * When reading, if we are FIFO, we send an ACK message on the same socket, so the other side
 * knows they can send the next message.
 * @author bmilojkovic
 *
 */
@SuppressWarnings("JavadocReference")
public class MessageUtil {

	/**
	 * Normally this should be true, because it helps with debugging.
	 * Flip this to false to disable printing every message send / receive.
	 */
	public static final boolean MESSAGE_UTIL_PRINTING = true;
	public static ConcurrentHashMap<Integer,Message> toBeDeliveredMessageMap = new ConcurrentHashMap<>();

	public static Message readMessage(Socket socket) {
		
		Message clientMessage = null;
			
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	
			clientMessage = (Message) ois.readObject();
			
			socket.close();
		} catch (IOException e) {
			AppConfig.timestampedErrorPrint("Error in reading socket on " +
					socket.getInetAddress() + ":" + socket.getPort());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (MESSAGE_UTIL_PRINTING) {
			AppConfig.timestampedStandardPrint("Got message " + clientMessage);
		}
				
		return clientMessage;
	}
	
	public static void sendMessage(Message message) {
		Thread delayedSender = new Thread(new DelayedMessageSender(message));
		delayedSender.start();
	}

	public static void addMessageToMap(Message message){
		if (!toBeDeliveredMessageMap.contains(message.getMessageId())){
			toBeDeliveredMessageMap.put(message.getMessageId(), message);
		} else {
			AppConfig.timestampedErrorPrint("addMessageToMap duplo dodavanje");
		}
	}

	public static boolean isDeliveredByID(int ID){
		if(toBeDeliveredMessageMap.containsKey(ID)){
			return toBeDeliveredMessageMap.get(ID).isDelivered();
		}
		AppConfig.timestampedErrorPrint("isDeliveredByKey poruka nije pronadjena");
		return false;
	}

	public static void deliverMessageByID(int ID){
		if(toBeDeliveredMessageMap.containsKey(ID)){
			toBeDeliveredMessageMap.get(ID).deliver();
		}else{
			AppConfig.timestampedErrorPrint("deliverMessageByID poruka nije pronadjena");
		}

	}

	public static void removeDeliveredMessageByID(int ID){
		toBeDeliveredMessageMap.remove(ID);
	}

	public static void sendDeliveredMessage(Message message){
		PongMessage pongMessage = new PongMessage(AppConfig.myServentInfo.getListenerPort(),
				message.getSenderPort(),
				PongType.DELIVERED, message.getMessageId());

		sendMessage(pongMessage);
	}
}
