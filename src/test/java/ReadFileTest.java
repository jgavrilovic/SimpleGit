import app.AppConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ReadFileTest {
//    public static void main(String[] args) {
//        try
//        {
//            File file=new File("src/main/resources/servent1/localStorage/a00.txt");    //creates a new file instance
//            FileReader fr=new FileReader(file);   //reads the file
//            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
//            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
//            String line;
//            while((line=br.readLine())!=null)
//            {
//                sb.append(line).append(" ");      //appends line to string buffer
//            }
//            fr.close();    //closes the stream and release the resources
//
//            System.out.println(sb.toString());
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//
//    }

    public static void main(String[] args) {
        //

        String gitFile = "src\\main\\resources\\servent0\\localRoot\\a1.txt";

        String path = gitFile.substring(0,gitFile.length()-5)+1+".txt";
        path =path.replace(path.substring(0,path.indexOf("localRoot")+9), "src/main/resources/servent1/localStorage");
        System.out.println(path);
    }
}
