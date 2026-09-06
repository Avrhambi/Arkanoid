package sprite;

import biuoop.DrawSurface;
import listener.Counter;
import game.GameLevel;
import listener.ScoreTrackingListener;

import java.awt.Color;
/**
 * The type ScoreIndicator.
 * present the score of the game on the screen
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */

public class ScoreIndicator implements Sprite {

    private final ScoreTrackingListener scoreTrackingListener;

    public static final int Y_FOR_SCORE_BOARD = 15;
    public static final int X_FOR_SCORE_BOARD = GameLevel.SCREEN_WIDTH / 2 - 10;
    public static final int FONT_SIZE = 15;

    /**
     * Constructor.
     */
    public ScoreIndicator() {
        this.scoreTrackingListener = new ScoreTrackingListener(new Counter());
    }

    /**
     * getter.
     *
     * @return the score tracking listener.
     */
    public ScoreTrackingListener getScoreTrackingListener() {
        return this.scoreTrackingListener;
    }

    /**
     * add the score tracking listener to the sprite list.
     *
     * @param game is the game.
     */
    public void addToGame(GameLevel game) {
        game.addSprite(this);
    }

    /**
     * draw the Score on the screen.
     *
     * @param d is the surface to draw on.
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.black);
        d.drawText(X_FOR_SCORE_BOARD, Y_FOR_SCORE_BOARD,
                "Score:" + this.scoreTrackingListener.getCounterCurrentScore().getValue(), FONT_SIZE);
    }

    /**
     * timePassed is related to the Sprite.Sprite interface. Implemented but don't do anything.
     */
    @Override
    public void timePassed() {

    }
}