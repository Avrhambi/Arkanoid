package listener;


import collidable.Block;
import game.GameLevel;
import sprite.Ball;
/**
 * The type BlockRemover.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class BlockRemover implements HitListener {

    private final GameLevel game;
    private final Counter remainingBlocks;

    public static final int NUM_TO_DECREASE = 1;

    /**
     * Constructor.
     *
     * @param game          is the game.
     * @param removedBlocks is the counter.
     */
    public BlockRemover(GameLevel game, Counter removedBlocks) {
        this.game = game;
        this.remainingBlocks = removedBlocks;
    }

    /**
     * @return the number of the ramin blocks.
     */
    public Counter getRemainingBlocks() {
        return remainingBlocks;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeHitListener(beingHit.getHitListeners().get(beingHit.getHitListeners().indexOf(this)));
        beingHit.removeFromGame(this.game);
        this.remainingBlocks.decrease(NUM_TO_DECREASE);
        this.game.getScoreTrackingListener().hitEvent(beingHit, hitter);
    }
}