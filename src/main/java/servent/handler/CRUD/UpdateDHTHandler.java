package servent.handler.CRUD;

import app.AppConfig;
import file.DHTFiles;
import file.GitFile;
import file.GitKey;
import servent.handler.MessageHandler;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.CRUD.UpdateDHTMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UpdateDHTHandler  implements MessageHandler {

    private Message clientMessage;
    public UpdateDHTHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
//        if (clientMessage.getMessageType() == MessageType.UPDATEDHT) {
//            GitFile gitFile = ((UpdateDHTMessage)clientMessage).getGitFile();
//            int target =((UpdateDHTMessage)clientMessage).getTarget();
//            try {
//                if(AppConfig.myServentInfo.getChordId()==target){
//                    AppConfig.timestampedErrorPrint("KRAJ");
//                }else{
//                    AppConfig.timestampedErrorPrint("Javio se cvor " + AppConfig.myServentInfo.getChordId() +" da dodaje u DHT " + gitFile);
//
//                    if(!DHTFiles.dhtFiles.containsKey(new GitKey(target))){
//                        ArrayList<GitFile> l = new ArrayList<>();
//                        l.add(gitFile);
//                        DHTFiles.dhtFiles.put(new GitKey(target), l);
//                    }else {
//                        for (Map.Entry<GitKey, List<GitFile>> entry0: DHTFiles.dhtFiles.entrySet()) {
//                            if(entry0.getKey().getRandNumber()==target){
//                                DHTFiles.dhtFiles.get(entry0.getKey()).add(gitFile);
//                            }
//                        }
//                    }
//
//                    UpdateDHTMessage updateDHTMessage = new UpdateDHTMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodePort(),
//                            gitFile, target);
//                    MessageUtil.sendMessage(updateDHTMessage);
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        } else {
//            AppConfig.timestampedErrorPrint("UPDATEDHT handler got a message that is not UPDATEDHT");
//        }
    }
}