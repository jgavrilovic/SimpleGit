public class HashNameOfFile {

    public static void main(String[] args) {
        String a = "a";
        String b = "ab";
        String c = "ab1ba";

        int sum=0;
        for (int i=0;i<c.length();i++){
            sum+=c.charAt(i);
        }
        System.out.println(sum%65);
    }
}
