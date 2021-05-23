package file;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LocalRoot {

    public static Queue<GitFile> workingRoot = new ConcurrentLinkedQueue<>();
}
