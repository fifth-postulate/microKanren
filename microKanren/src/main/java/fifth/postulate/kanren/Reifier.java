package fifth.postulate.kanren;

public interface Reifier<T> {
    void accept(Substitution<T> substitution);
}
