package cli.command;

import app.AppConfig;
import cli.command.CRUD.PullCommand;
import file.LocalRoot;
import file.LocalStorage;
import servent.handler.CRUD.TellPullHandler;
import team.LocalTeam;

public class InfoCommand implements CLICommand {

	@Override
	public String commandName() {
		return "info";
	}

	@Override
	public void execute(String args) {

		AppConfig.timestampedStandardPrint("My info: " + AppConfig.myServentInfo);
		AppConfig.timestampedStandardPrint("LocalStorage: " + LocalStorage.storage);
		AppConfig.timestampedStandardPrint("LocalRoot: " + LocalRoot.workingRoot);
		AppConfig.timestampedStandardPrint("LastModified: " + TellPullHandler.lastModifiedTimeFiles);
		AppConfig.timestampedStandardPrint("LocalTeams: " + LocalTeam.teams);
		AppConfig.timestampedStandardPrint("Team files: " + PullCommand.listaTimskihFajlova);
	}

}
