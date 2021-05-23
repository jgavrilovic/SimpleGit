package file;

import lombok.Data;

import java.io.File;
import java.io.Serializable;


@Data
public class GitFile implements Serializable {

    private static final long serialVersionUID = -1490336670619402477L;
    private String name;
    private int version;

    public GitFile(String name) {
        this.name = name;
        this.version = 0;
    }

    public GitFile(String name, int version) {
        this.name = name;
        this.version = version;
    }
}
