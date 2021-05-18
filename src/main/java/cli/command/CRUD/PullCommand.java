package cli.command.CRUD;


import app.AppConfig;
import cli.command.CLICommand;

import file.DHTFiles;
import file.GitFile;
import servent.message.AskPullMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.util.ConcurrentModificationException;


public class PullCommand  implements CLICommand {

    //pull naziv_fajla [version]

    @Override
    public String commandName() {
        return "pull";
    }


    /**TODO
     * Treba da se dohvati najveca verzija*/
    @Override
    public void execute(String args) {
        final String fileName;
        final int version;

        String[] commands = args.split(" ");
        if(commands.length==2){
            fileName = args.split(" ")[0];
            version = Integer.parseInt(args.split(" ")[1]);
        }else if(commands.length==1){
            fileName = args.split(" ")[0];
            version=0;
        }else {
            fileName=null;
            version=-1;
            AppConfig.timestampedErrorPrint("Pull komanda treba da ima 2 argumenta, naziv i opciono verziju");
        }


       try{
           DHTFiles.dhtFiles.forEach((k, v) -> {
               v.stream().filter(x->x.getName().equals(fileName) && x.getVersion()== version).forEach(gitFile -> {
                   AppConfig.timestampedErrorPrint("saljem " + fileName + " " + k.getRandNumber());
                   Message askPull = new AskPullMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(k.getRandNumber()).getListenerPort(),fileName+" "+version,k.getRandNumber());
                   MessageUtil.sendMessage(askPull);
                   AppConfig.timestampedErrorPrint("posalo sam za datoteku na cvor " + fileName + " " + k.getRandNumber());
               });
           });
       }catch (ConcurrentModificationException e){
           e.printStackTrace();
       }


    }
}