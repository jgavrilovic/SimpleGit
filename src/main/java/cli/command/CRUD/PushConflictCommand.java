package cli.command.CRUD;

import cli.command.CLICommand;

public class PushConflictCommand implements CLICommand {

    /**
     *
     * posaljem @ConflictMessage biraj opcije, null
     * vrati mi view - posaljem komanda view, gitgile()
     * vrati mi push - posaljem DONE_PUSH, null ->upisem njegovu
     * vrati mi pull - posaljem DONE_Pull, gitfile() -> update
     *
     * */

    @Override
    public String commandName() {
        return "push-conflict";
    }


    @Override
    public void execute(String args) {

    }
}