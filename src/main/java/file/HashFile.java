package file;

import app.AppConfig;
import app.ChordState;

public class HashFile {
    public static int hashFileName(String fileName){

        int sum=0;
        for (int i=0;i<fileName.length();i++){
            sum+=fileName.charAt(i);
        }
        return sum% ChordState.CHORD_SIZE;
    }
}
