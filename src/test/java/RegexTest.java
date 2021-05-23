import app.AppConfig;
import servent.handler.CRUD.ConflictHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {




        String path = "//src/servent0/a2.txt";
        System.out.println(path.replaceAll("\\d\\.","."));
//        Pattern p = Pattern.compile("\\d.");
//        Matcher m = p.matcher(path);
//        m.find();
//        int a = Integer.parseInt(m.group());
//        System.out.println(a);

    }

}
