package servent;

import app.AppConfig;
import app.Cancellable;
import servent.handler.*;
import servent.handler.CRUD.*;
import servent.handler.FAILURE.*;
import servent.handler.START.*;
import servent.handler.TEAM_COMM.TeamInfoHandler;
import servent.handler.TEAM_COMM.TellPullTeamHandler;
import servent.handler.TEAM_ORG.TeamHandler;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServentListener implements Runnable, Cancellable {

	private volatile boolean working = true;
	
	public SimpleServentListener() {
		
	}

	/*
	 * Thread pool for executing the handlers. Each client will get it's own handler thread.
	 */
	private final ExecutorService threadPool = Executors.newWorkStealingPool();
	
	@Override
	public void run() {
		ServerSocket listenerSocket = null;
		try {
			listenerSocket = new ServerSocket(AppConfig.myServentInfo.getListenerPort(), 100);
			/*
			 * If there is no connection after 1s, wake up and see if we should terminate.
			 */
			listenerSocket.setSoTimeout(1000);
		} catch (IOException e) {
			AppConfig.timestampedErrorPrint("Couldn't open listener socket on: " + AppConfig.myServentInfo.getListenerPort());
			System.exit(0);
		}
		
		
		while (working) {
			try {
				Message clientMessage;
				
				Socket clientSocket = listenerSocket.accept();
				
				//GOT A MESSAGE! <3
				clientMessage = MessageUtil.readMessage(clientSocket);
				
				MessageHandler messageHandler = new NullHandler(clientMessage);
				

				switch (clientMessage.getMessageType()) {
					case NEW_NODE:
						messageHandler = new NewNodeHandler(clientMessage);
						break;
					case WELCOME:
						messageHandler = new WelcomeHandler(clientMessage);
						break;
					case SORRY:
						messageHandler = new SorryHandler(clientMessage);
						break;
					case UPDATE:
						messageHandler = new UpdateHandler(clientMessage);
						break;
					case QUIT:
						messageHandler = new NodeQuitHandler(clientMessage);
						break;
				//HANDLER ZA CRUD OPERACIJE
					case ADD_GITFILE:
						messageHandler = new AddHandler(clientMessage);
						break;
					case ASK_PULL:
						messageHandler = new AskPullHandler(clientMessage);
						break;
					case TELL_PULL:
						messageHandler = new TellPullHandler(clientMessage);
						break;
					case COMMIT:
						messageHandler = new CommitHandler(clientMessage);
						break;
					case DELETE:
						messageHandler = new RemoveHandler(clientMessage);
						break;
					case CONFLICT:
						messageHandler = new ConflictHandler(clientMessage);
						break;
				//---------------------------

				//HANDLER ZA OTKAZE
					case PING:
						messageHandler = new PingHandler(clientMessage);
						break;
					case PONG:
						messageHandler = new PongHandler(clientMessage);
						break;

					case CLONE_GITFILE:
						messageHandler = new CloneFileHandler(clientMessage);
						break;
				//---------------------------


				//HANDLER ZA TIMOVE
					case TEAM_UPDATE:
						messageHandler = new TeamHandler(clientMessage);
						break;
					case TELL_PULL_TEAM:
						messageHandler = new TellPullTeamHandler(clientMessage);
						break;
					case TEAM_INFO:
						messageHandler = new TeamInfoHandler(clientMessage);
						break;
				//---------------------------



				}
				
				threadPool.submit(messageHandler);
			} catch (SocketTimeoutException timeoutEx) {
				//Uncomment the next line to see that we are waking up every second.
//				AppConfig.timedStandardPrint("Waiting...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		this.working = false;
	}

}
