package animation;

import biuoop.DrawSurface;
import game.GameLevel;
import sprite.Sprite;

import java.awt.Color;
/**
 * The type SunAnimation.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class SunAnimation implements Sprite {

    private Sprite sprite;

    /**
     * Constructor.
     *
     * @param sprite1 is the sprite name.
     */
    public SunAnimation(Sprite sprite1) {
        this.sprite = sprite1;
    }

    @Override
    public void drawOn(DrawSurface d) {

        // draw on the sprite.
        sprite.drawOn(d);

        // drawing the sun.
        d.setColor(Color.decode("#efe7b0"));
        d.fillCircle(130, 130, 50);
        for (int i = 0; i < 80; i++) {
            d.drawLine(130, 130, GameLevel.SCREEN_WIDTH - GameLevel.SCREEN_FRAME_SIZE - (10 * i), 250);
        }
        d.setColor(Color.decode("#ecd749"));
        d.fillCircle(130, 130, 40);
        d.setColor(Color.decode("#ffe118"));
        d.fillCircle(130, 130, 30);

    }

    @Override
    public void timePassed() {
    }

    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}