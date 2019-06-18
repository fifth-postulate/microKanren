package fifth.postulate.kanren;

import java.util.*;
import java.util.function.Function;

import static fifth.postulate.kanren.Term.variable;

/**
 * A state is a pair of a substitution and fresh variable
 */
public class State<T> {
    public static <V> State<V> empty() {
        return new State();
    }

    private final Variable fresh;
    private final Substitution<T> substitution;

    private State() {
        this.fresh = Variable.first();
        this.substitution = Substitution.empty();
    }

    private State(Variable fresh, Substitution<T> substitution) {
        this.fresh = fresh;
        this.substitution = substitution;
    }

    public Term<T> walk(Term<T> term) {
        if (substitution.containsKey(term)) {
            Term<T> association = substitution.get(term);
            return walk(association);
        } else {
            return term;
        }
    }

    public Optional<State<T>> unify(Term<T> left, Term<T> right) {
        Term<T> leftWalk = walk(left);
        Term<T> rightWalk = walk(right);
        if (leftWalk.equals(rightWalk)) {
            return Optional.of(this);
        }
        if (leftWalk.isVariable()) {
            Variable variable = ((Var) leftWalk).variable;
            Substitution<T> substitution = Substitution.extend(variable, rightWalk, this.substitution);
            return Optional.of(this.with(substitution));
        }
        if (rightWalk.isVariable()) {
            Variable variable = ((Var) rightWalk).variable;
            Substitution<T> substitution = Substitution.extend(variable, leftWalk, this.substitution);
            return Optional.of(this.with(substitution));
        }
        return Optional.empty();
    }

    private State<T> with(Substitution<T> substitution) {
        return new State(this.fresh, substitution);
    }

    public Stream<T> apply(Function<Term<T>, Goal<T>> f) {
        return f.apply(Var.iable(fresh)).apply(next());
    }

    private State<T> next() {
        return new State(fresh.next(), substitution);
    }

    public void reify(Reifier<T> reifier) {
        reifier.accept(substitution);
    }
}

interface Substitution<T> {
    static <V> Substitution<V> empty() {
        return Empty.instance();
    }

    static <V> Substitution<V> extend(Variable variable, Term<V> term, Substitution<V> chain) {
        return Association.extend(variable, term, chain);
    }

    boolean containsKey(Term<T> term);

    Term<T> get(Term<T> term);

    Collection<Term<T>> keys();
}

class Association<T> implements Substitution {
    public static <V> Substitution<V> extend(Variable variable, Term<V> term, Substitution<V> chain) {
        return new Association(variable, term, chain);
    }

    private final Term<T> key;
    private final Term<T> association;
    private final Substitution<T> chain;

    private Association(Variable variable, Term<T> term, Substitution<T> chain) {
        this.key = variable(variable);
        this.association = term;
        this.chain = chain;
    }

    @Override
    public boolean containsKey(Term term) {
        return key.equals(term) || chain.containsKey(term);
    }

    @Override
    public Term get(Term term) {
        if (key.equals(term)) {
            return association;
        } else {
            return chain.get(term);
        }
    }

    @Override
    public Collection<Term<T>> keys() {
        List<Term<T>> keys = Arrays.asList(key);
        keys.addAll(chain.keys());
        return keys;
    }
}

class Empty<T> implements Substitution<T> {
    private static Substitution instance;

    public static <T> Substitution<T> instance() {
        if (instance == null) {
            instance = new Empty();
        }
        return (Substitution<T>) instance;
    }

    @Override
    public boolean containsKey(Term<T> term) {
        return false;
    }

    @Override
    public Term<T> get(Term<T> term) {
        return term;
    }

    @Override
    public Collection<Term<T>> keys() {
        return Collections.emptyList();
    }
}


