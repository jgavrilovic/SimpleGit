package cli.command.CRUD;

import app.AppConfig;
import cli.command.CLICommand;
import file.GitFile;
import file.HashFile;
import file.LocalRoot;
import servent.message.CRUD.AddMessage;
import servent.message.util.MessageUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AddCommand implements CLICommand {

    @Override
    public String commandName() {
        return "add";
    }

    @Override
    public void execute(String args) {
        //izvucem iz argumenta ime trazene datoteke file/dir
        String fullPath=AppConfig.myServentInfo.getRootPath()+"/"+args;

        //hashiram po nazivu fajla
        AppConfig.timestampedStandardPrint(args);
        int destination = HashFile.hashFileName(args);

        //proveravam da li je id moj ili ne i saljem
        if(destination!=AppConfig.myServentInfo.getChordId()){
            AppConfig.timestampedErrorPrint("Izabran cvor: " + destination);
            sendFile(fullPath,destination,true);
        }else{
            AppConfig.timestampedErrorPrint("Izabran cvor si sam ti " + destination);
            sendFile(fullPath,destination,false);
        }

    }

    /**Cita sadrzaj datoteke, potreban input je putanja do fajla*/
    public static String fileReader(String path)   {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            AppConfig.timestampedErrorPrint("Ne moze da se pronadje datoteka na toj putanji!");
        }
        return sb.toString();
    }

    /**Slanje fila ili celog direktorijuma na datu putanju, potrebni argumenti
     * putanja do file/dir, id primaoca, flag za tip datoteke*/
    public  void sendFile(String fullPath, int key, boolean flag){
            try {
                Files.walk(Paths.get(fullPath)).filter(Files::isRegularFile).forEach(a->{
                    File f = new File(a.toFile().getPath());
                    LocalRoot.workingRoot.add(new GitFile(f.getName(),f.getPath(),0));
                    String name = f.getPath().substring(f.getPath().indexOf("localRoot")+10);
                    if(flag) {
                        AppConfig.timestampedErrorPrint(name);
                        AddMessage addMsg = new AddMessage(
                                AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodeForKey(key).getListenerPort(), name, fileReader(f.getPath()),AppConfig.myServentInfo.getListenerPort(), key);
                        MessageUtil.sendMessage(addMsg);
                    }
                });
            } catch (SecurityException s) {
                AppConfig.timestampedErrorPrint("Nedozvoljen pristup fajlu");
            } catch (IOException e){
                AppConfig.timestampedErrorPrint("Direktorijum ne moze da se otvori");
            }
    }

}

//saljem ili marko.txt ili dir1/marko.txt