package cli.command.CRUD;


import app.AppConfig;
import cli.command.CLICommand;
import file.HashFile;
import servent.message.CRUD.AskPullMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.util.ConcurrentModificationException;


public class PullCommand  implements CLICommand {

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
            version=-2;
        }else {
            fileName="";
            version=-1;
            AppConfig.timestampedErrorPrint("Pull komanda treba da ima 2 argumenta, naziv i opciono verziju");
        }

        //salje se zahtev za datoteku na odredjen id, pomocu hash funkcije


        try{
            if(fileName.contains(".txt")){
                int key = HashFile.hashFileName(fileName.substring(0,fileName.indexOf(".")-1)+".txt");
                AppConfig.timestampedStandardPrint("Pocinje pretraga za datoteku!");
//               DHTFiles.dhtFiles.forEach((k, v) -> {
//                   v.stream().filter(x->x.getName().contains(fileName) && x.getVersion()== version).forEach(gitFile -> {

                Message askPull = new AskPullMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),fileName+" "+version,key);
                MessageUtil.sendMessage(askPull);
                AppConfig.timestampedErrorPrint("Datoteka " + fileName + " poslana na cvor: " + key);
//                   });
//               });
            }else{
                int key = HashFile.hashFileName(fileName);
                AppConfig.timestampedStandardPrint("Pocinje pretraga za direktorijum!");
//               DHTFiles.dhtFiles.forEach((k, v) -> {
//                   v.stream().filter(x->x.getName().contains(fileName)).forEach(gitFile -> {

                Message askPull = new AskPullMessage(AppConfig.myServentInfo.getListenerPort(),AppConfig.chordState.getNextNodeForKey(key).getListenerPort(),fileName +" "+version,key);
                MessageUtil.sendMessage(askPull);
                AppConfig.timestampedErrorPrint("Datoteka " + fileName + " poslana na cvor: " + key);
//
//                   });
//               });
            }
        }catch (ConcurrentModificationException e){
            e.printStackTrace();
        }


    }
}