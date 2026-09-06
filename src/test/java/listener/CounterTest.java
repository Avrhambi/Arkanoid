package listener;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Characterization tests for {@link Counter}, a plain mutable int holder.
 */
class CounterTest {

    @Test
    void noArgConstructor_startsAtZero() {
        assertEquals(0, new Counter().getValue());
    }

    @Test
    void intConstructor_startsAtGivenValue() {
        assertEquals(7, new Counter(7).getValue());
        assertEquals(-3, new Counter(-3).getValue());
    }

    @Test
    void increase_addsToValue() {
        Counter c = new Counter(2);
        c.increase(5);
        assertEquals(7, c.getValue());
    }

    @Test
    void decrease_subtractsAndGoesNegative() {
        Counter c = new Counter(2);
        c.decrease(5);
        assertEquals(-3, c.getValue());
    }

    @Test
    void increaseAndDecrease_compose() {
        Counter c = new Counter();
        c.increase(10);
        c.decrease(3);
        c.increase(1);
        assertEquals(8, c.getValue());
    }
}
