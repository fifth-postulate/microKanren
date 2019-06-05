package fifth.postulate.kanren;

import java.util.HashMap;
import java.util.Map;

/**
 * A state is a pair of a substitution and fresh variable
 */
public class State<T> {
    public static <V> State<V> empty() {
        return new State();
    }

    private final Variable fresh;
    private final Map<Variable, Term<T>> substitution;

    private State() {
        this.fresh = Variable.first();
        this.substitution = new HashMap();
    }
}
