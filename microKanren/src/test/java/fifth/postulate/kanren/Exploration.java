package fifth.postulate.kanren;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Exploration {
    private Goal<Integer> goal;

    @Before
    public void createGoal() {
        goal = Goal.callFresh(term -> {
            return Goal.identical(term, Term.value(5));
        });
    }

    @Test
    public void identicalGoalShouldProduceANonEmptyStream() {
        Stream<Integer> stream = goal.apply(State.empty());

        assertFalse(stream.isEmpty());
    }

   @Test
    public void identicalGoalShouldProduceANonStreamWithOneState() {
        Stream<Integer> stream = goal.apply(State.empty());

        Collection<State<Integer>> states = stream.take(10);

        assertEquals(1, states.size());
    }
    @Test
    public void identicalGoalShouldProduceANonStreamWithOneStateWithCorrectAssociation() {
        Stream<Integer> stream = goal.apply(State.empty());
        Collection<State<Integer>> states = stream.take(10);

        State<Integer> firstState = states.iterator().next();
        VariableMap<Integer> map = VariableMap.create();
        firstState.reify(map);

        assertEquals(Term.value(5), map.map.get(0));
    }
}

class VariableMap<T> implements Reifier<T> {
    public static <V> VariableMap<V> create() {
        return new VariableMap();
    }

    public final Map<Integer, Term<T>> map = new HashMap<Integer, Term<T>>();

    private VariableMap() {
        /* hide constructor; use static create method. */
    }

    @Override
    public void accept(Substitution<T> substitution) {
        for (Term<T> key : substitution.keys()) {
            Term<T> term = substitution.get(key);
            if (key.isVariable() && !term.isVariable()) {
                Integer index = ((Var) key).variable.index;
                this.map.put(index, term);
            }
        }
    }
}
