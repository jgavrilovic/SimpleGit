package servent.handler.CRUD;

import app.AppConfig;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.CRUD.ConflictMessage;
import servent.message.CRUD.ConflictType;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.FileWriter;
import java.io.IOException;


public class ConflictHandler  implements MessageHandler {

    private Message clientMessage;
    public ConflictHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    public static int target =0;
    public static String fileProblem;
    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.CONFLICT) {
            try {

                int key = ((ConflictMessage)clientMessage).getTarget();
                if (AppConfig.chordState.isKeyMine(key)) {
                    ConflictType txt = ((ConflictMessage)clientMessage).getConflictType();
                    target =((ConflictMessage)clientMessage).getSenderID();

                    if(txt==ConflictType.WARNING){
                        AppConfig.timestampedStandardPrint(txt + " doslo je do konflikta... " + ((ConflictMessage) clientMessage).getName());
                        AppConfig.timestampedStandardPrint("Izaberi jednu od opcija: view, push ili pull");
                        fileProblem=((ConflictMessage) clientMessage).getName();  //src\main\resources\servent1\localStorage\dir1\b0.txt

                    }
                    else if(txt==ConflictType.VIEW){
                        AppConfig.timestampedStandardPrint(txt + " je izabrana opcija...");

                        String name = CommitHandler.statFIleName;
                        String content = CommitHandler.statFIleCont;
                        int version = CommitHandler.statFIleVersion;

                        AppConfig.timestampedErrorPrint(name+" "+content+" "+version);
                        ConflictMessage conflictMessage1 = new ConflictMessage(
                                AppConfig.myServentInfo.getListenerPort(),
                                AppConfig.chordState.getNextNodeForKey(((ConflictMessage)clientMessage).getSenderID()).getListenerPort(),
                                name ,
                                content,
                                version,
                                ConflictType.DONE_VIEW,
                                AppConfig.myServentInfo.getChordId(),
                                ConflictHandler.target);
                        MessageUtil.sendMessage(conflictMessage1);
                    }
                    else if(txt==ConflictType.DONE_VIEW){
                        AppConfig.timestampedStandardPrint("Izaberi jednu od opcija: view, push ili pull");

                        String name = ((ConflictMessage) clientMessage).getName();
                        String content = ((ConflictMessage) clientMessage).getContent();
                        int version = ((ConflictMessage) clientMessage).getVersion();
                        AppConfig.timestampedErrorPrint("Putanja i naziv datoteke iz skladista: " +  name);
                        AppConfig.timestampedErrorPrint("Sadrzaj u konfliktu: " +  content);
                        AppConfig.timestampedErrorPrint("Verzija: " +  version);
                    }
                    else if(txt==ConflictType.PUSH) {
                        AppConfig.timestampedStandardPrint(txt + " je izabrana opcija...");
                        String name = ((ConflictMessage) clientMessage).getName();
                        String content = ((ConflictMessage) clientMessage).getContent();
                        AppConfig.timestampedStandardPrint(name);
                        AppConfig.timestampedStandardPrint(content);


                        /**TODO
                         *  Odavde vidim da kada radim push ne uzimam verziju u obzir!
                         *  Sending message ConflictMessage{conflictType=PUSH, senderID=4, target=48, name='src/main/resources/servent1/localRoot/a2.txt', content='ser2
                         *  Got message     ConflictMessage{conflictType=PUSH, senderID=4, target=48, name='src/main/resources/servent1/localRoot/a2.txt', content='ser2', version=0}
                         *
                         * */
                        LocalStorage.storage.stream().filter(f -> f.getPath().contains(name)).iterator().forEachRemaining(o -> {
                            AppConfig.timestampedStandardPrint("Datoteka se prepisuje");
                            try {
                                FileWriter myWriter = new FileWriter(name);
                                myWriter.write(content);
                                myWriter.close();
                                AppConfig.timestampedStandardPrint("prepisan sadrzaj datoteke!");
                            } catch (IOException e) {
                                AppConfig.timestampedErrorPrint("Doslo je do greske prilikom upisa u fajl: " + name);
                            }
                        });
                    }
                    else if(txt==ConflictType.PULL){
                        AppConfig.timestampedStandardPrint("PULL " + CommitHandler.statFIleName);
                        AppConfig.timestampedStandardPrint("PULL " + CommitHandler.statFIleCont);
                        ConflictMessage conflictMessage = new ConflictMessage(
                                AppConfig.myServentInfo.getListenerPort(),
                                AppConfig.chordState.getNextNodeForKey(ConflictHandler.target).getListenerPort(),
                                CommitHandler.statFIleName,
                                CommitHandler.statFIleCont,
                                CommitHandler.statFIleVersion,
                                ConflictType.DONE_PULL,
                                AppConfig.myServentInfo.getChordId(),
                                ConflictHandler.target);
                        MessageUtil.sendMessage(conflictMessage);

                    }else if(txt==ConflictType.DONE_PULL){
                        AppConfig.timestampedStandardPrint(((ConflictMessage) clientMessage).getName());
                        try {
                            /**TODO
                             *  PAZI NA VERZIJU, ako je DIR/asd.txt onda se ne cita ono DIR*/
                            AppConfig.timestampedStandardPrint("Datoteka se sinhronizuje!");
                            String[] nameAr = ((ConflictMessage) clientMessage).getName().split("\\\\");
                            String myPath = AppConfig.myServentInfo.getRootPath()+"/"+nameAr[nameAr.length-1];
                            AppConfig.timestampedStandardPrint(myPath);

                            FileWriter myWriter = new FileWriter(myPath);
                            myWriter.write(((ConflictMessage) clientMessage).getContent());
                            myWriter.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }


                    }else {
                        AppConfig.timestampedErrorPrint(txt + " ovo nije predvidjeno ponasanje");
                    }


                }else {
                    ConflictMessage conflictMessage = new ConflictMessage(
                            AppConfig.myServentInfo.getListenerPort(),
                            AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                            ((ConflictMessage)clientMessage).getName(),
                            ((ConflictMessage)clientMessage).getContent(),
                            ((ConflictMessage) clientMessage).getVersion(),
                            ((ConflictMessage)clientMessage).getConflictType(),
                            ((ConflictMessage)clientMessage).getSenderID(), key);
                    MessageUtil.sendMessage(conflictMessage);
                }

            }  catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AppConfig.timestampedErrorPrint("Conflict handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }
    }

}