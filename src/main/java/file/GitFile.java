package file;

import lombok.Data;

import java.io.File;
import java.io.Serializable;


@Data
public class GitFile implements Serializable {

    private static final long serialVersionUID = -1490336670619402477L;
    private String name;
    private File file;
    private int version;

    public GitFile(String name,File file) {
        this.name = name;
        this.file=file;
        this.version = 0;
    }

    public GitFile(String name, File file, int version) {
        this.name = name;
        this.file=file;
        this.version = version;
    }
}
