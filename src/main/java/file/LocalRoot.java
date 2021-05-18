package file;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRoot {

    public static ConcurrentHashMap<GitKey, List<GitFile>> workingRoot = new ConcurrentHashMap<>();
}
