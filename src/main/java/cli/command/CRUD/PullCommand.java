package cli.command.CRUD;


import app.AppConfig;
import cli.command.CLICommand;
import file.GitFile;
import file.HashFile;
import servent.message.CRUD.AskPullMessage;
import servent.message.util.MessageUtil;

import java.util.ConcurrentModificationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class PullCommand  implements CLICommand {

    public static ConcurrentHashMap<Integer, GitFile> listaTimskihFajlova = new ConcurrentHashMap<>();

    @Override
    public String commandName() {
        return "pull";
    }

    @Override
    public void execute(String args) {
        final String fileName;
        final int version;

        //Odvaja naziv datoteke od (opciono) verzije, ako verzija nije proslednjena default = -2
        String[] commands = args.split(" ");
        if(commands.length==2){
            fileName = args.split(" ")[0];
            version = Integer.parseInt(args.split(" ")[1]);
        }else if(commands.length==1){
            fileName = args.split(" ")[0];
            version=-2; //znak da uzimam najvecu verziju
        }else {
            fileName="";
            version=-1;
            AppConfig.timestampedErrorPrint("Pull komanda treba da ima 2 argumenta, naziv i opciono verziju");
        }

        //salje se zahtev za datoteku na odredjen id, pomocu hash funkcije
        try{
            AtomicBoolean flag = new AtomicBoolean(true);

            //----------------------------part2
            listaTimskihFajlova.entrySet().stream().forEach(f->{
                AppConfig.timestampedErrorPrint(f.getValue().getName()+"  "+fileName);
                if(f.getValue().getName().contains(fileName) && f.getValue().getVersion()==version){
                    AppConfig.timestampedErrorPrint("usao");
                    flag.set(false);

                    AskPullMessage askPull = new AskPullMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(f.getKey()).getListenerPort(),fileName+" "+version,f.getKey(),AppConfig.myServentInfo.getTeamName());
                    MessageUtil.sendMessage(askPull);
                    AppConfig.timestampedErrorPrint("Datoteka " + fileName + " poslana na cvor: " + f.getKey());

                }
            });
            //----------------------------


            if(flag.get()){
                int key;
                if(fileName.split("\\\\").length==2){
                    key = HashFile.hashFileName(fileName);
                }else{
                    key = HashFile.hashFileName(fileName.split("\\\\")[0]);
                }
                AppConfig.timestampedStandardPrint("Pocinje pretraga za datoteku! " + fileName);
                AskPullMessage askPull = new AskPullMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),fileName+" "+version,key,AppConfig.myServentInfo.getTeamName());
                MessageUtil.sendMessage(askPull);
                AppConfig.timestampedErrorPrint("Datoteka " + fileName + " poslana na cvor: " + key);
            }

        }catch (ConcurrentModificationException e){
            e.printStackTrace();
        }


    }
}