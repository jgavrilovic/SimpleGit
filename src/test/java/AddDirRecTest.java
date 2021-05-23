import java.io.File;
import java.io.IOException;

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
