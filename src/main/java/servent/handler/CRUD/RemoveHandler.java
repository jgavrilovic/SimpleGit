package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import file.GitFile;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.CRUD.RemoveMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class RemoveHandler implements MessageHandler {

    private Message clientMessage;

    public RemoveHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.DELETE) {
            int key = ((RemoveMessage)clientMessage).getTarget();
            if (AppConfig.chordState.isKeyMine(key)) {
                try {
                    List<GitFile> listaIndexa = new ArrayList<>();
                    LocalStorage.storage.stream().filter(o->o.getName().contains(clientMessage.getMessageText())).forEach(p->{
                        listaIndexa.add(p);
                        AppConfig.timestampedStandardPrint("Datoteka " + p + " je uspesno obrisana!");
                    });
                    listaIndexa.forEach(o->LocalStorage.storage.remove(o));




                    Files.walk(Paths.get(AppConfig.myServentInfo.getStoragePath())).filter(Files::isRegularFile).forEach(a->{
                        if(a.toFile().getPath().replaceAll("\\d\\.",".").contains(clientMessage.getMessageText())){
                            AppConfig.timestampedStandardPrint(a.toFile().getName() +" je obrisana: " +a.toFile().delete());
                        }
                    });
                }catch (Exception e){
                    AppConfig.timestampedErrorPrint("Doslo je do greske: ");
                    e.printStackTrace();
                }

            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                RemoveMessage removeMessage = new RemoveMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(), clientMessage.getMessageText(),
                        ((RemoveMessage)clientMessage).getSenderID(), ((RemoveMessage) clientMessage).getTarget()
                );
                MessageUtil.sendMessage(removeMessage);
            }
        } else {
            AppConfig.timestampedErrorPrint("Delete handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}