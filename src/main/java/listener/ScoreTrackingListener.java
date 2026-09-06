package listener;

import collidable.Block;
import sprite.Ball;
/**
 * The type ScoreTrackingListener.
 * keep track of the game score
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class ScoreTrackingListener implements HitListener {

    private final Counter currentScore;

    public static final int HIT_VALUE_IN_SCORE = 5;

    /**
     * Constructor.
     *
     * @param scoreCounter is the score counter.
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    /**
     * @return the score counter.
     */
    public Counter getCounterCurrentScore() {
        return currentScore;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        this.currentScore.increase(HIT_VALUE_IN_SCORE);
    }
}
