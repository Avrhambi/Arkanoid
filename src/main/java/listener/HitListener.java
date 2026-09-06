package listener;

import collidable.Block;
import sprite.Ball;
/**
 * The interface HitListener.
 *
 * @author Avraham Bicha
 * @since 2023-06-07
 *
 */
public interface HitListener {
    /**
     * Called whenever the {@code beingHit} object is hit.
     *
     * @param beingHit the block that was hit
     * @param hitter   the {@link Ball} that did the hitting
     */
    void hitEvent(Block beingHit, Ball hitter);
}
