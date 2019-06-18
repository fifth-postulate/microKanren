package fifth.postulate.kanren;


import java.util.function.Function;

public interface Goal<T> extends Function<State<T>, Stream<T>> {
    static <V> Goal<V> identical(Term<V> left, Term<V> right) {
        return (state) -> {
            return state.unify(left, right)
                     .map(s -> Stream.unit(s))
                     .orElse(Stream.empty());
        };
    }
}
