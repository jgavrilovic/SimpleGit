import app.AppConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class AddFileTest {
    public static void main(String[] args) {

        File file = new File("C:\\Users\\Jovan Gavrilovic\\Desktop\\odgovori.txt");
        FileReader ins = null;
        try {
            ins = new FileReader(file);
            ins.close();
        } catch (IOException e) {
            AppConfig.timestampedErrorPrint("Doslo je do greske u otvaranju file: " + file.getName() + " " +e.getMessage());
        }

        try {
            Files.lines(file.toPath())
                    .map(s -> s.trim())
                    .filter(s -> !s.isEmpty())
                    .forEach(System.out::println);
        } catch (IOException e) {
            AppConfig.timestampedErrorPrint("Doslo je do greske u citanju file: " + file.getName() + " " +e.getMessage());
        }
    }
}
