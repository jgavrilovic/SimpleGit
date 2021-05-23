import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MoveFileTest {
    public static void main(String[] args) {


        /*
         File f = new File("test/../test.txt");
        try {
            f.createNewFile();
            System.out.println(f.getPath());
            System.out.println(f.getAbsolutePath());
            System.out.println(f.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        try {
            File file = new File("src/main/resources/servent1/localRoot/moja.txt");
            Files.move( Paths.get( file.getAbsolutePath() ) , Paths.get("src/main/resources/servent2/localStorage/moja.txt"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
