package file;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LocalStorage {


    public static Queue<GitFile> storage = new ConcurrentLinkedQueue<>();
}
