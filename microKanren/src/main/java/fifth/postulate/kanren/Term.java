package fifth.postulate.kanren;

import java.util.Objects;

/**
 * Terms of the language consist of variables, values deemed identical under
 * equals, and pairs of the foregoing.
 */
public interface Term<T> {
    static <V> Term<V> value(V value) {
        return Value.containing(value);
    }

    static <V> Term<V> variable(Variable variable) {
        return Var.iable(variable);
    }
}

class Value<T> implements Term<T> {
    public static <V> Value<V> containing(V value) {
        return new Value(value);
    }

    private final T value;

    private Value(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value<?> value1 = (Value<?>) o;
        return Objects.equals(value, value1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

class Var<T> implements Term<T> {
    public static <V> Term<V> iable(Variable variable) {
        return new Var(variable);
    }

    private final Variable variable;

    private Var(Variable variable) {
        this.variable = variable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Var<?> var = (Var<?>) o;
        return Objects.equals(variable, var.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable);
    }
}
