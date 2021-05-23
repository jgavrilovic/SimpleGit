import file.GitFile;
import file.LocalStorage;

public class StreamMaxVersion {
    public static void main(String[] args) {
//        LocalStorage.storage.add(new GitFile("a",0));
//
//        LocalStorage.storage.add(new GitFile("b",5));
//        LocalStorage.storage.add(new GitFile("a",2));
//        LocalStorage.storage.add(new GitFile("d",0));
//        LocalStorage.storage.add(new GitFile("c",0));
//        LocalStorage.storage.add(new GitFile("a",1));
//        LocalStorage.storage.add(new GitFile("b",4));
//        LocalStorage.storage.add(new GitFile("a",5));
//
//        int m =LocalStorage.storage.stream().filter(x -> x.getName().contains("c")).mapToInt(GitFile::getVersion).filter(o -> o >= 0).max().orElse(0);
//        System.out.println(m);


        //PULL COMMAND
        String a = "a.txt";
        String b = "a0.txt";

        System.out.println(b.substring(0,b.indexOf(".")-1)+".txt");
    }
}
