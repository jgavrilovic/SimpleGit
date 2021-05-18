package file;

import lombok.Data;

@Data
public class GitKey {

    private int randNumber;

    public GitKey(int randNumber) {
        this.randNumber = randNumber;
    }

}
