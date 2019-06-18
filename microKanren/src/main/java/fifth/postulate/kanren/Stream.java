package fifth.postulate.kanren;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static fifth.postulate.kanren.MatureStream.matureStream;

/**
 * A sequences of states is a _stream_.
 *
 * A goal's success may result in a sequence of (enlarged) states, which we term a
 * stream. The result of a μKanren program is a stream of satisfying states. The
 * stream may be finite or infinite, as there may be finite or infinitely many
 * satisfying states
 */
public interface Stream<T> {
    static <V> Stream<V> empty() {
        return EmptyStream.Singleton;
    }

    static <V> Stream<V> mature(State<V> state, Stream<V> tail) {
        return matureStream(state, tail);
    }

    static <V> Stream<V> unit(State<V> state) {
        return mature(state, empty());
    }

    boolean isEmpty();

    Collection<State<T>> take(int i);
}

enum EmptyStream implements fifth.postulate.kanren.Stream {
    Singleton;

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Collection<State> take(int n) {
        return Collections.emptyList();
    }
}

class MatureStream<T> implements Stream<T> {
    public static <V> Stream<V> matureStream(State<V> state, Stream<V> tail) {
        return new MatureStream(state, tail);
    }

    private final State<T> state;
    private final Stream<T> tail;

    private MatureStream(State<T> state, Stream<T> tail) {
        this.state = state;
        this.tail = tail;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Collection<State<T>> take(int n) {
        if (n <= 0) {
            return Collections.emptyList();
        } else {
            List<State<T>> states = Arrays.asList(state);
            states.addAll(tail.take(n - 1));
            return states;
        }
    }
}
