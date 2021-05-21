package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import file.GitFile;
import file.LocalRoot;
import servent.message.CRUD.AddMessage;
import servent.message.util.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AddCommand implements CLICommand {

    @Override
    public String commandName() {
        return "add";
    }

    @Override
    public void execute(String args) {
        int rand = (int) (Math.random() * AppConfig.chordState.getAllNodeInfo().size());


        String fullPath=AppConfig.myServentInfo.getRootPath()+"/"+args;
        ArrayList<GitFile> listaGitFileova = new ArrayList<>();
        int destination = AppConfig.chordState.getAllNodeInfo().get(rand).getChordId();

        if(destination!=AppConfig.myServentInfo.getChordId()){
            AppConfig.timestampedErrorPrint("Izabran cvor: " + destination);
            sendFile(fullPath,destination,true,listaGitFileova);
        }else{
            AppConfig.timestampedErrorPrint("Izabran cvor si sam ti");
            sendFile(fullPath,destination,false,listaGitFileova);
        }

    }


    private void sendFile(String fullPath, int key, boolean flag, ArrayList<GitFile> listaGitFileova){
        /**proveri da li je dir*/
        if(fullPath.contains(".txt")){
            File f = new File(fullPath);
            LocalRoot.workingRoot.add(new GitFile(f.getName(),f));
            if(flag){
                AddMessage addMsg = new AddMessage(
                        AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(key).getListenerPort(), f,AppConfig.myServentInfo.getListenerPort(), key);
                MessageUtil.sendMessage(addMsg);
            }
            String name = f.getName().substring(0,f.getName().length()-4)+"0.txt";
            listaGitFileova.add(new GitFile(name,f));
        }else{
            try {
                Files.walk(Paths.get(fullPath)).filter(Files::isRegularFile).forEach(a->{

                    File f = new File(a.toFile().getPath());
                    LocalRoot.workingRoot.add(new GitFile(f.getName(),f));

                    if(flag) {
                        AddMessage addMsg = new AddMessage(
                                AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodeForKey(key).getListenerPort(), f,AppConfig.myServentInfo.getListenerPort(), key);
                        MessageUtil.sendMessage(addMsg);
                    }

                    String name = f.getName().substring(0,f.getName().length()-4)+"0.txt";
                    listaGitFileova.add(new GitFile(name,f));

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}