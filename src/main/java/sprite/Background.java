package sprite;

import biuoop.DrawSurface;
import collidable.Block;
import game.GameLevel;
import geometry.Rectangle;

import java.awt.Color;

/**
 * The type Background.
 * The basic background of the game
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class Background implements Sprite {

    private final Rectangle rectangle;
    private final Color color;

    /**
     * Constructor.
     *
     * @param rectangle is the rectangle.
     * @param color     is the main color for the background.
     */
    public Background(Rectangle rectangle, Color color) {
        this.rectangle = rectangle;
        this.color = color;
    }

    @Override
    public void drawOn(DrawSurface d) {
        Block b = new Block(rectangle, this.color);
        b.drawOn(d);
    }

    @Override
    public void timePassed() {

    }

    @Override
    public void addToGame(GameLevel g) {

    }
}
