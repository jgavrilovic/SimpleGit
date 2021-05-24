package cli.command.INFO;

import app.AppConfig;
import app.ServentInfo;
import cli.command.CLICommand;

public class SuccessorInfo implements CLICommand {

	@Override
	public String commandName() {
		return "successor_info";
	}

	@Override
	public void execute(String args) {
		ServentInfo[] successorTable = AppConfig.chordState.getSuccessorTable();

		int num = 0;
		for (ServentInfo serventInfo : successorTable) {
			System.out.println(num + ": " + serventInfo);
			num++;
		}

		System.out.println(AppConfig.chordState.getPredecessor());

	}

}
