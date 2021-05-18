public class HashTest {
    public static void main(String[] args) {

        int chord_size = 14;

        int half = chord_size/2;

        for(int i=0;i<chord_size;i++){
            if(i%2==0 && i<half){
                System.out.println(i%chord_size +" "+Math.abs(("/txt"+i).hashCode() % 4));
            }else if(i%2==1 && i<half){
                System.out.println((half-1+i)%chord_size+" "+Math.abs(("/txt"+i).hashCode() % 4));
            }

            if(i%2==0 && i>=half){
                System.out.println((i+1)%chord_size +" "+Math.abs(("/txt"+i).hashCode() % 4));
            }else if(i%2==1 && i>=half){
                System.out.println((half+i)%chord_size+" "+Math.abs(("/txt"+i).hashCode() % 4));
            }

        }


    }
}
//0,1,2,3,4,5,6,7
//8,9,10,11,12,13,14,15