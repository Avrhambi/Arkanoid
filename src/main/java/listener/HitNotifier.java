package listener;

/**
 * The interface HitNotifier.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-07
 *
 */
public interface HitNotifier {
    // Add hl as a listener to hit events.
    void addHitListener(HitListener hl);
    // Remove hl from the list of listeners to hit events.
    void removeHitListener(HitListener hl);
}
