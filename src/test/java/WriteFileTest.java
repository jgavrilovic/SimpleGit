import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class WriteFileTest {
    public static void main(String[] args) {
        try {

            File myObj = new File("src/main/resources/servent2/localStorage/moja.txt");
            Scanner myReader = new Scanner(myObj);
            StringBuilder stringBuilder = new StringBuilder();
            while (myReader.hasNextLine()) {
                stringBuilder.append(myReader.next()+ " ");

            }
            myReader.close();

            FileWriter myWriter = new FileWriter("src/main/resources/servent2/localStorage/moja.txt");
            myWriter.write(stringBuilder.toString()+ " Files in Java might be tricky, but it is fun enough!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
