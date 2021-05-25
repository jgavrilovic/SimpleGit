import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class WordTest {
    public static void main(String[] args) {
        String a = "dir1\\\\c.txt";

       // dir1\\c.txt

       // dir1\c.txt

        System.out.println(a.replace("\\\\","\\"));

    }
}
