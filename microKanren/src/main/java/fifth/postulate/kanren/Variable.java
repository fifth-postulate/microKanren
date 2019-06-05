package fifth.postulate.kanren;

import java.util.Objects;

/**
 * Variable indices are identified by integers.
 */
public class Variable {
    public static Variable first() {
        return new Variable(0);
    }

    private final int index;

    private Variable(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return index == variable.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
