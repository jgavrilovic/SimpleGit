import app.AppConfig;
import servent.handler.CRUD.ConflictHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {



        Pattern p = Pattern.compile("\\d");
        String path = "a2.txt";
        Matcher m = p.matcher(path);
        m.find();
        int a = Integer.parseInt(m.group());
        System.out.println(a);

    }

}
