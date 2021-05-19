import app.AppConfig;
import servent.message.AddMessage;
import servent.message.util.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class AddDirRecTest {
    public static void main(String[] args) throws IOException {


        try {

           boolean a = new File("src/main/resources/servent0/localStorage/dir1/ir/asdf.txt").mkdirs();
            System.out.println(a);

        }catch (Exception e){
            e.printStackTrace();
        }


        //Files.move(Paths.get("./src/main/resources/servent0/localRoot/dir2/dir2.2/moja.txt"), Paths.get("src/main/resources/servent0/localStorage/moja.txt"), StandardCopyOption.REPLACE_EXISTING);
    }


}
