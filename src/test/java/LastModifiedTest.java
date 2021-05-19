import java.io.File;

public class LastModifiedTest {
    public static void main(String[] args) {
        File f = new File("src/main/resources/servent1/localStorage/dir1/moja0.txt");
        System.out.println(f.lastModified());
    }
}
