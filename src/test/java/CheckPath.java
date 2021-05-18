import java.io.File;
import java.io.IOException;

public class CheckPath {
    public static void main(String[] args) throws IOException {
        String fileSeparator = System.getProperty("file.separator");

        String dir = "./src/main/resources/moja.txt";

        //absolute file name with path
        String absoluteFilePath = dir+fileSeparator+"file.txt";
        File file = new File(absoluteFilePath);
        if(file.createNewFile()){
            System.out.println(absoluteFilePath+" File Created");
        }else
            System.out.println("File "+absoluteFilePath+" already exists");


    }
}
