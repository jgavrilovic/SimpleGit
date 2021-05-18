package cli.command.CRUD;

import app.AppConfig;
import app.ServentInfo;
import cli.command.CLICommand;
import file.*;
import servent.message.AddMessage;
import servent.message.util.MessageUtil;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AddCommand implements CLICommand {

    //add naziv_fajla
    //add naziv_dir

    /**Logika je takva da kada dodam datoteku u sistem ja je tek tada dodam i na radni koren
     * zato sto ako budem zeleo da povucem istu tu datoteku a verzija se promenila treba da dodje do konflikta u putanji*/

    /**TODO
     *  Napravi poruku koja ce obavestiti sve da je neka datoteka negde u sistemu da bi svi imali
     *  @dhtFiles azuriran, za sada radi pull na onome ko je add datoteku
     * */
    @Override
    public String commandName() {
        return "add";
    }

    @Override
    public void execute(String args) {

        int rand = (int) (Math.random() * AppConfig.chordState.getAllNodeInfo().size());//0,1
        int key;
        String fullPath=AppConfig.myServentInfo.getRootPath()+"/"+args;


        AppConfig.timestampedErrorPrint(rand+"");
        List<GitFile> listaGitFileova = new ArrayList<>();
        if(AppConfig.chordState.getAllNodeInfo().get(rand).getChordId()!=AppConfig.myServentInfo.getChordId()){
            key = AppConfig.chordState.getAllNodeInfo().get(rand).getChordId();
            List<File>  list = sendFile(fullPath,key,true);
            for (File f: list) {
                String name = f.getName().substring(0,f.getName().length()-4)+"0.txt";
                listaGitFileova.add(new GitFile(name,f));
            }
        }else{
            AppConfig.timestampedErrorPrint("Izabran cvor si sam ti");
            key=AppConfig.myServentInfo.getChordId();
            List<File>  list = sendFile(fullPath,key,false);
            for (File f: list) {
                String name = f.getName().substring(0,f.getName().length()-4)+"0.txt";
                listaGitFileova.add(new GitFile(name,f));
            }
        }
        LocalRoot.workingRoot.put(new GitKey(key), listaGitFileova);
        DHTFiles.dhtFiles.put(new GitKey(key), listaGitFileova);

        AppConfig.timestampedErrorPrint("------------------------");
        AppConfig.timestampedErrorPrint(LocalStorage.storage.toString());
        AppConfig.timestampedErrorPrint(DHTFiles.dhtFiles.toString());
    }



    private List<File> sendFile(String fullPath, int key, boolean flag){
        AppConfig.timestampedErrorPrint(fullPath);
        if(fullPath.contains(".txt")){
            List<File> listOfFiles = new ArrayList<>();
            File f = new File(fullPath);
            if(flag){
                AddMessage addMsg = new AddMessage(
                        AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                        f,key);
                MessageUtil.sendMessage(addMsg);
            }
            listOfFiles.add(f);
            return listOfFiles;
        }else{
            try {
                List<File> listOfFiles = new ArrayList<>();
                Files.walk(Paths.get(fullPath))
                        .filter(Files::isRegularFile)
                        .forEach(a->{
                            File f = new File(a.toFile().getAbsolutePath());
                            if(flag) {
                                AddMessage addMsg = new AddMessage(
                                        AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),
                                        f, key);
                                MessageUtil.sendMessage(addMsg);
                            }
                            listOfFiles.add(f);
                        });
                return listOfFiles;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        AppConfig.timestampedStandardPrint("Komanda izvrsena");
        return null;
    }
}
