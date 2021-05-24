package servent.handler.START;

import app.AppConfig;
import servent.handler.MessageHandler;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.START.UpdateMessage;
import servent.message.START.WelcomeMessage;
import servent.message.TeamMessage;
import servent.message.util.MessageUtil;
import team.LocalTeam;

import java.util.ArrayList;

public class WelcomeHandler implements MessageHandler {

	private Message clientMessage;

	public WelcomeHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.WELCOME) {
			WelcomeMessage welcomeMsg = (WelcomeMessage)clientMessage;

			AppConfig.chordState.init(welcomeMsg);

			UpdateMessage um = new UpdateMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePort(), "");
			MessageUtil.sendMessage(um);

		} else {
			AppConfig.timestampedErrorPrint("Welcome handler got a message that is not WELCOME");
		}

	}

}
