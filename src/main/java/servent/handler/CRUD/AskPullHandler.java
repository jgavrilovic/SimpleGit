package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import cli.command.CRUD.AddCommand;
import file.GitFile;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.CRUD.AskPullMessage;
import servent.message.CRUD.TellPullMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class AskPullHandler  implements MessageHandler {

    private Message clientMessage;

    public AskPullHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ASK_PULL) {
            int key = ((AskPullMessage)clientMessage).getTarget();
            //proverava se krajnja destinacija
            if (AppConfig.chordState.isKeyMine(key)) {

                //cita se iz poruke ime i verzija
                String name = clientMessage.getMessageText().split(" ")[0];
                int version = Integer.parseInt(clientMessage.getMessageText().split(" ")[1]);

                AppConfig.timestampedStandardPrint("Dosao je zahtev za povlacenje datoteke pod nazivom: " + name + " i verzije: " + version);

                //trazi se najveca moguca verzija
                int maxVersion=0;
                if(version==-2){
                    maxVersion = LocalStorage.storage.stream().filter(x -> x.getName().contains(name)).mapToInt(GitFile::getVersion).filter(o -> o >= 0).max().orElse(0);
                }else {
                    maxVersion=version;
                }

                //u listu fajlova se dodaju oni koji od govaraju zahtevima
                List<GitFile> gitfiles = new ArrayList<>();
                int finalMaxVersion = maxVersion;
                if(name.contains(".txt"))
                    LocalStorage.storage.stream().filter(x->x.getName().contains(name) && x.getVersion()== finalMaxVersion).forEach(gitfiles::add);
                else
                    LocalStorage.storage.stream().filter(x->x.getName().contains(name)).forEach(gitfiles::add);


                AppConfig.timestampedStandardPrint("Lista odgovarajucih fajlova za slanje: " + gitfiles.toString());
                for (GitFile file: gitfiles) {
                    TellPullMessage tgm = new TellPullMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(),
                            file.getName(), AddCommand.fileReader(file.getName()), file.getVersion());
                    MessageUtil.sendMessage(tgm);
                }

            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                AskPullMessage agm = new AskPullMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(),
                        clientMessage.getMessageText(),((AskPullMessage)clientMessage).getTarget());
                MessageUtil.sendMessage(agm);
            }
        } else {
            AppConfig.timestampedErrorPrint("Ask handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}