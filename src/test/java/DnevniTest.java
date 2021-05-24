import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DnevniTest {
    public static void main(String[] args) {

        Set<Integer> hasset = new HashSet<>();
        hasset.add(5);
        hasset.add(3);
        hasset.add(5);
        hasset.add(3);
        hasset.add(1);
        ConcurrentHashMap<String,Set<Integer>> mapa = new ConcurrentHashMap<>();
        mapa.put("A",hasset);

        Set<Integer> hasset1 = new HashSet<>();
        hasset1.add(10);
        hasset1.add(8);
        hasset1.add(5);
        hasset1.add(4);
        hasset1.add(1);

        ConcurrentHashMap<String,Set<Integer>> mapa1 = new ConcurrentHashMap<>();
        mapa1.put("A",new HashSet<>(hasset1));

        mapa.entrySet().stream().forEach(e->{
            Set<Integer> unique = new HashSet<>();
            unique.addAll(mapa.get(e.getKey()));
            unique.addAll(mapa1.get(e.getKey()));

            mapa1.put(e.getKey(),unique);
        });

        System.out.println(mapa1);
    }
}
