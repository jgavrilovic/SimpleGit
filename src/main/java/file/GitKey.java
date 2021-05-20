package file;

import lombok.Data;

import java.util.Objects;

@Data
public class GitKey {

    private int randNumber;

    public GitKey(int randNumber) {
        this.randNumber = randNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitKey gitKey = (GitKey) o;
        return randNumber == gitKey.randNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(randNumber);
    }
}
