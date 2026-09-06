package sprite;

import biuoop.DrawSurface;
import game.GameLevel;

/**
 * The interface Sprite.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -05-07
 *
 */
public interface Sprite {
    /**
     * draw the sprite to the screen.
     *
     * @param d the DrawSurface , the screen
     */
    void drawOn(DrawSurface d);

    /**
     * notify the sprite that time has passed.
     */
    void timePassed();

    /**
     * Add object to teh game instance.
     *
     * @param g the game instance
     */
    void addToGame(GameLevel g) throws Exception;
//    void addToGame(Game g);
}
