package listener;

/**
 * The type Counter.
 *
 * @author Avraham Bicha
 * @since 2023-06-14
 */
public class Counter {

    private int counter;

    /**
     * Constructor.
     */
    public Counter() {
        this.counter = 0;
    }


    /**
     * Constructor.
     *
     * @param num is the initial amount for the counter.
     */
    public Counter(int num) {
        this.counter = num;
    }

    /**
     * add number to current count.
     *
     * @param number is the amount to add.
     */
    public void increase(int number) {
        this.counter += number;
    }

    /**
     * subtract number from current count.
     *
     * @param number is the amount to subtract.
     */
    public void decrease(int number) {
        this.counter -= number;
    }

    /**
     * @return current count.
     */
    public int getValue() {
        return this.counter;
    }
}
