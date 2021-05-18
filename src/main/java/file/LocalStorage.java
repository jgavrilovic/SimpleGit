package file;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LocalStorage {

    public static ConcurrentHashMap<GitKey, List<GitFile>> storage = new ConcurrentHashMap<>();
}
