package listener;

/**
 * The interface HitNotifier.
 *
 * @author Avraham Bicha
 * @since 2023-06-07
 *
 */
public interface HitNotifier {
    /**
     * Adds a listener to be notified of hit events.
     *
     * @param hl the listener to add
     */
    void addHitListener(HitListener hl);

    /**
     * Removes a listener so it is no longer notified of hit events.
     *
     * @param hl the listener to remove
     */
    void removeHitListener(HitListener hl);
}
