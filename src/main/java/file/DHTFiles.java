package file;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DHTFiles {

    public static ConcurrentHashMap<GitKey, List<GitFile>> dhtFiles = new ConcurrentHashMap<>();
}
