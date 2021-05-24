package servent.handler.CRUD;

import app.AppConfig;
import app.ServentInfo;
import cli.command.CRUD.AddCommand;
import file.GitFile;
import file.LocalStorage;
import servent.handler.MessageHandler;
import servent.message.CRUD.CommitMessage;
import servent.message.CRUD.ConflictMessage;
import servent.message.CRUD.ConflictType;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;
import team.LocalTeam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class CommitHandler implements MessageHandler {

    private Message clientMessage;

    public CommitHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    public static String statFIleName;
    public static String statFIleCont;
    public static int statFIleVersion;
    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.COMMIT) {
            int key = ((CommitMessage)clientMessage).getTarget();
            if (AppConfig.chordState.isKeyMine(key)) {
                try {
                    String name = ((CommitMessage)clientMessage).getNameOfFile();//src/../root/a2.txt || rc/../root/dir1/a2.txt
                    String content = ((CommitMessage)clientMessage).getContentOfFile();
                    int version = ((CommitMessage)clientMessage).getVersion();


                    String[] splitPath = name.split("\\\\");

                    Queue<GitFile> temp = new ConcurrentLinkedQueue<>();

                    String fileName = splitPath[splitPath.length-1];//a.replaceAll("[0-9]", "")

                    AppConfig.timestampedErrorPrint(name+" "+content+" "+version+" "+fileName);


                    AtomicInteger conflict = new AtomicInteger(0);
                    LocalStorage.storage.stream().filter(f-> f.getName().contains(fileName.replaceAll("[0-9]\\.", "."))).forEach(o->{
                        if(o.getVersion()==version)
                            conflict.set(1);
                    });


                    if(conflict.get()==0) {
                        //ako nije ista verzija cuvam u storage kao naziv: naziv+verzija , verzija
                        AppConfig.timestampedStandardPrint("nije ista verzija");
                        String path = name.replaceAll("[0-9]\\.", version+".");
                        path=path.substring(path.indexOf("localRoot")).replace("localRoot\\","");

                        path = AppConfig.myServentInfo.getStoragePath()+"/"+path;
                        File file = new File(path);
                        try {
                            boolean isCreated = file.createNewFile();
                            if(isCreated){
                                AppConfig.timestampedStandardPrint("Dodata nova datoteka u sistem " + file.getName() );
                            }

                            FileWriter myWriter = new FileWriter(file);
                            myWriter.write(content);
                            myWriter.close();
                            AppConfig.timestampedStandardPrint("prepisan sadrzaj datoteke!");
                        } catch (IOException e) {
                            AppConfig.timestampedErrorPrint("Doslo je do problema pri kopiranju ili kreiranju fajla " +e.getMessage() );
                        }
                        ConcurrentHashMap<String,Integer> teamPulls = new ConcurrentHashMap<>();
                        LocalTeam.teams.entrySet().stream().forEach(e->{
                            teamPulls.put(e.getKey(),0);
                        });
                        temp.add(new GitFile(fileName.replaceAll("[0-9]\\.", "."),file.getPath(),version, teamPulls));
                    }else{
                        LocalStorage.storage.stream().filter(f-> f.getName().equals(fileName.replaceAll("[0-9]\\.", "."))
                                && f.getVersion()==version).forEach(o->{
                            int target = ((CommitMessage) clientMessage).getSenderID();
                            statFIleName = o.getName();
                            statFIleCont = AddCommand.fileReader(o.getPath());
                            statFIleVersion=o.getVersion();
                            AppConfig.timestampedErrorPrint(statFIleName+" "+statFIleCont+" "+statFIleVersion);

                            //salje se poruka za konflikt i ocekuje se da izabere nacin kako da resi konflikt
                            String[] nameAr = o.getName().split("\\\\");
                            String myName = AppConfig.myServentInfo.getRootPath()+"/"+nameAr[nameAr.length-1];
                            ConflictMessage conflictMessage = new ConflictMessage(
                                    AppConfig.myServentInfo.getListenerPort(),
                                    AppConfig.chordState.getNextNodeForKey(target).getListenerPort(),
                                    myName,"",
                                    ConflictType.WARNING,
                                    AppConfig.myServentInfo.getChordId(), target
                            );
                            MessageUtil.sendMessage(conflictMessage);
                            conflict.set(0);
                        });
                    }
                    LocalStorage.storage.addAll(temp);
                    temp.clear();

                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                CommitMessage agm = new CommitMessage(
                        clientMessage.getSenderPort(),
                        nextNode.getListenerPort(),
                        ((CommitMessage)clientMessage).getNameOfFile(),
                        ((CommitMessage)clientMessage).getContentOfFile(),
                        ((CommitMessage)clientMessage).getVersion(),
                        ((CommitMessage)clientMessage).getSenderID(),
                        ((CommitMessage)clientMessage).getTarget());
                MessageUtil.sendMessage(agm);
            }
        } else {
            AppConfig.timestampedErrorPrint("Commit handler je prihvatio poruku :" + clientMessage.getMessageType() +" GRESKA!");
        }

    }
}