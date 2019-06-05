package fifth.postulate.kanren;

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

    public Term<T> walk(Term<T> term) {
        if (substitution.containsKey(term)) {
            Term<T> association = substitution.get(term);
            return walk(association);
        } else {
            return term;
        }
    }
}

interface Substitution<T> {
    static <V> Substitution<V> empty() {
        return Empty.instance();
    }

    static <V> Substitution<V> extend(Variable variable, Term<V> term, Substitution<V> chain) {
        return extend(variable, term, chain);
    }

    boolean containsKey(Term<T> term);

    Term<T> get(Term<T> term);
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
}


