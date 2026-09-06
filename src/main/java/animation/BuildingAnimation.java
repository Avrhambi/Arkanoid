package animation;

import biuoop.DrawSurface;
import game.GameLevel;
import sprite.Sprite;

import java.awt.Color;
/**
 * The type BuildingAnimation.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class BuildingAnimation implements Sprite {

    private final Sprite sprite;

    /**
     * Constructor.
     *
     * @param sprite1 is the sprite name.
     */
    public BuildingAnimation(Sprite sprite1) {
        this.sprite = sprite1;
    }

    @Override
    public void drawOn(DrawSurface d) {
        // draw on the sprite.
        sprite.drawOn(d);

        // drawing the building.
        d.setColor(Color.black);
        d.fillRectangle(75, 450, 100, 150);
        d.setColor(Color.white);
        d.fillRectangle(88, 463, 75, 150);
        d.setColor(Color.black);
        for (int i = 0; i < 5; i++) {
            d.fillRectangle(75, 450 + (2 * i * 15), 100, 10);
        }
        for (int i = 0; i < 4; i++) {
            d.fillRectangle(75 + (2 * i * 15), 450, 10, 150);
        }
        d.setColor(Color.DARK_GRAY);
        d.fillRectangle(120, 250, 10, 200);
        d.setColor(Color.decode("#3e3a39"));
        d.fillRectangle(110, 400, 30, 50);
        d.setColor(Color.decode("#d8ac66"));
        d.fillCircle(125, 235, 15);
        d.setColor(Color.decode("#f64d36"));
        d.fillCircle(125, 235, 10);
        d.setColor(Color.white);
        d.fillCircle(125, 235, 5);
    }

    @Override
    public void timePassed() {
    }

    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}