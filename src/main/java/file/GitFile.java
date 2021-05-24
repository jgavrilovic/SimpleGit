package file;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;


@Getter
public class GitFile implements Serializable {

    private static final long serialVersionUID = -1490336670619402477L;
    private String name;
    private String path;
    private int version;
    private ConcurrentHashMap<String,Integer> teamPulls;
    public GitFile(String name,String path, int version) {
        this.name = name;
        this.path=path;
        this.version = version;
        this.teamPulls=new ConcurrentHashMap<>();
    }

    public GitFile(String name,String path, int version, ConcurrentHashMap<String,Integer> teamPulls) {
        this.name = name;
        this.path=path;
        this.version = version;
        this.teamPulls=teamPulls;
    }

    @Override
    public String toString() {
        return "{"+ name + " " + path + " " + version + " " + teamPulls+"}";
    }
}
