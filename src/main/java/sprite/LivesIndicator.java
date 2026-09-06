package sprite;

import biuoop.DrawSurface;
import listener.Counter;
import game.GameLevel;

import java.awt.Color;
/**
 * The type LivesIndicator.
 * present the number of remaining lives on the screen.
 * @author Avraham Bicha
 * @since 2026-09-06
 */

public class LivesIndicator implements Sprite {

    private final Counter lives;

    public static final int Y_FOR_LIVES = 15;
    public static final int X_FOR_LIVES = 100;
    public static final int FONT_SIZE = 15;

    /**
     * Constructor.
     *
     * @param lives is the shared lives counter, owned by GameFlow.
     */
    public LivesIndicator(Counter lives) {
        this.lives = lives;
    }

    /**
     * getter.
     *
     * @return the lives counter.
     */
    public Counter getLives() {
        return this.lives;
    }

    /**
     * add the lives indicator to the sprite list.
     *
     * @param game is the game.
     */
    public void addToGame(GameLevel game) {
        game.addSprite(this);
    }

    /**
     * draw the number of lives on the screen.
     *
     * @param d is the surface to draw on.
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.black);
        d.drawText(X_FOR_LIVES, Y_FOR_LIVES, "Lives: " + this.lives.getValue(), FONT_SIZE);
    }

    /**
     * timePassed is related to the Sprite.Sprite interface. Implemented but don't do anything.
     */
    @Override
    public void timePassed() {

    }
}
