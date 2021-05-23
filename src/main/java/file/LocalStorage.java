package file;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LocalStorage {


    public static Queue<GitFile> storage = new ConcurrentLinkedQueue<>();
}
