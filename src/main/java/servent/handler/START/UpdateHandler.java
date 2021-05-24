package servent.handler.START;

import app.AppConfig;
import app.ServentInfo;
import servent.handler.MessageHandler;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.START.UpdateMessage;
import servent.message.TEAM_ORG.TeamMessage;
import servent.message.util.MessageUtil;
import team.LocalTeam;

import java.util.ArrayList;
import java.util.List;

public class UpdateHandler implements MessageHandler {

	private Message clientMessage;

	public UpdateHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}

	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.UPDATE) {
			if (clientMessage.getSenderPort() != AppConfig.myServentInfo.getListenerPort()) {
				ServentInfo newNodInfo = new ServentInfo("localhost", clientMessage.getSenderPort());
				List<ServentInfo> newNodes = new ArrayList<>();
				newNodes.add(newNodInfo);

				AppConfig.chordState.addNodes(newNodes);
				String newMessageText = "";
				if (clientMessage.getMessageText().equals("")) {
					newMessageText = String.valueOf(AppConfig.myServentInfo.getListenerPort());
				} else {
					newMessageText = clientMessage.getMessageText() + "," + AppConfig.myServentInfo.getListenerPort();
				}
				Message nextUpdate = new UpdateMessage(clientMessage.getSenderPort(), AppConfig.chordState.getNextNodePort(),
						newMessageText);
				MessageUtil.sendMessage(nextUpdate);
			} else {
				TeamMessage teamMessage = new TeamMessage(
						AppConfig.myServentInfo.getListenerPort(),
						AppConfig.chordState.getNextNodePort(),
						AppConfig.myServentInfo.getChordId(),
						AppConfig.myServentInfo.getTeamName(),
						AppConfig.myServentInfo.getChordId(),LocalTeam.teams);
				MessageUtil.sendMessage(teamMessage);

				String messageText = clientMessage.getMessageText();
				String[] ports = messageText.split(",");

				List<ServentInfo> allNodes = new ArrayList<>();
				for (String port : ports) {
					allNodes.add(new ServentInfo("localhost", Integer.parseInt(port)));
				}
				AppConfig.chordState.addNodes(allNodes);
			}


			if(AppConfig.chordState.getAllNodeInfo().size()==2){

			}
		} else {
			AppConfig.timestampedErrorPrint("Update message handler got message that is not UPDATE");
		}
	}

}
