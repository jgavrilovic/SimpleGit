package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import file.HashFile;
import file.LocalRoot;
import servent.handler.CRUD.TellPullHandler;
import servent.message.CRUD.CommitMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class CommitCommand implements CLICommand {


    @Override
    public String commandName() {
        return "commit";
    }


    @Override
    public void execute(String args)  {
        //odvajam naziv datoteke
        String fullPath;
        if(args!=null){
            fullPath = AppConfig.myServentInfo.getRootPath()+"/"+args;
        }else{
            fullPath = AppConfig.myServentInfo.getRootPath();
        }

        AppConfig.timestampedStandardPrint("komituje se: " + fullPath);
        sendCommitMessage(args,fullPath);

    }


    private int key=0;
    private void sendCommitMessage(String  args,String  fullPath) {
        AtomicInteger ver = new AtomicInteger(0);
        try {
            //prolazi kroz radni koren
            Files.walk(Paths.get(fullPath))
                    .filter(Files::isRegularFile)
                    .forEach(a->{
                        //proverava da li je datoteka menjana
                        TellPullHandler.lastModifiedTimeFiles.entrySet().stream().filter(x-> x.getKey().getPath().equals(a.toFile().getPath())).forEach(fs->{

                            key=HashFile.hashFileName(args.replaceAll("[0-9]\\.","."));

                            //uzima odgovarajucu verziju takve datoteke
                            LocalRoot.workingRoot.stream().filter(
                                    t->t.getPath().equals(a.toFile().getPath())).forEach(o->{
                                ver.set(o.getVersion());
                            });

                            //salje se poruka sa odgovarajucom verzijom
                            if(fs.getValue()==a.toFile().lastModified()){
                                CommitMessage msg = new CommitMessage(
                                        AppConfig.myServentInfo.getListenerPort(),
                                        AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                                        a.toFile().getPath(),  //src/../root/a2.txt || rc/../root/dir1/a2.txt
                                        AddCommand.fileReader(a.toFile().getPath()),
                                        ver.get(),
                                        AppConfig.myServentInfo.getChordId(),key);
                                MessageUtil.sendMessage(msg);
                            }else {
                                ver.getAndIncrement();
                                CommitMessage msg = new CommitMessage(
                                        AppConfig.myServentInfo.getListenerPort(),
                                        AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                                        a.toFile().getPath(),
                                        AddCommand.fileReader(a.toFile().getPath()),
                                        ver.get(),
                                        AppConfig.myServentInfo.getChordId(),key);
                                MessageUtil.sendMessage(msg);
                            }
                        });
                    });
        } catch (IOException e) {
            AppConfig.timestampedErrorPrint("Takva datoteka ne postoji!");
        }
    }
}
