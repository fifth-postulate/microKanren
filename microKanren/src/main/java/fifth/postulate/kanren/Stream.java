package fifth.postulate.kanren;

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
}

enum EmptyStream implements fifth.postulate.kanren.Stream {
    Singleton
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
}
