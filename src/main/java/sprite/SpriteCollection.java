package sprite;

import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;
/**
 * The type Sprite collection.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -05-07
 *
 */
public class SpriteCollection {

    private final List<Sprite> spriteList;

    /**
     * Constructor.
     */
    public SpriteCollection() {
        this.spriteList = new ArrayList<>();
    }

    /**
     * The method will add a new sprite for the list.
     *
     * @param s is the sprite to add.
     */
    public void addSprite(Sprite s) {
        this.spriteList.add(s);
    }

    /**
     * The method will activate the timePassed method on each sprites in the list.
     */
    public void notifyAllTimePassed() {
        java.util.List<Sprite> clone = new ArrayList<>(this.spriteList);
        for (Sprite sprite : clone) {
            sprite.timePassed();
        }
    }

    /**
     * @return the sprite list.
     */
    public List<Sprite> getSpriteList() {
        return this.spriteList;
    }

    /**
     * The method will draw all the sprites on the screen.
     *
     * @param d is the draw surface to draw on.
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite sprite : spriteList) {
            sprite.drawOn(d);
        }
    }
}