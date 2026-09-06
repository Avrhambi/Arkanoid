package listener;

import collidable.Block;
import game.GameLevel;
import sprite.Ball;

/**
 * The type BallRemover.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class BallRemover implements HitListener {

    private final GameLevel game;
    private final Counter remainingBalls;

    public static final int NUM_TO_DECREASE = 1;

    /**
     * Constructor.
     *
     * @param game           is the game.
     * @param remainingBalls is the counter.
     */
    public BallRemover(GameLevel game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    /**
     * getter for Counter.
     *
     * @return the number of balls in the game.
     */
    public Counter getRemainingBalls() {
        return this.remainingBalls;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(this.game);
        this.remainingBalls.decrease(NUM_TO_DECREASE);
    }
}