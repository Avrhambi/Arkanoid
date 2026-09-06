package animation;

import biuoop.DrawSurface;
import game.GameLevel;
import level.DirectHit1Level;
import sprite.Sprite;

import java.awt.Color;
/**
 * The type TargetAnimation.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class TargetAnimation implements Sprite {

    private final Sprite sprite;

    /**
     * Constructor.
     *
     * @param sprite1 is the sprite name.
     */
    public TargetAnimation(Sprite sprite1) {
        this.sprite = sprite1;
    }

    @Override
    public void drawOn(DrawSurface d) {

        // draw on the sprite.
        sprite.drawOn(d);

        // drawing the target.
        d.setColor(Color.blue);
        d.drawLine(GameLevel.SCREEN_WIDTH / 2 - GameLevel.SCREEN_FRAME_SIZE + 35,
                GameLevel.SCREEN_HEIGHT / 3 + 170,
                GameLevel.SCREEN_WIDTH / 2 - GameLevel.SCREEN_FRAME_SIZE + 35,
                GameLevel.SCREEN_HEIGHT / 3 - 150);

        d.drawLine(DirectHit1Level.SQUARE_Y_VALUE + DirectHit1Level.SQUARE_VALUE,
                (DirectHit1Level.SQUARE_Y_VALUE - DirectHit1Level.SQUARE_VALUE / 2),
                DirectHit1Level.SQUARE_X_VALUE + DirectHit1Level.SQUARE_Y_VALUE,
                (DirectHit1Level.SQUARE_Y_VALUE - DirectHit1Level.SQUARE_VALUE / 2));

        d.drawCircle(DirectHit1Level.SQUARE_X_VALUE + (DirectHit1Level.SQUARE_VALUE / 2),
                DirectHit1Level.SQUARE_Y_VALUE - (DirectHit1Level.SQUARE_VALUE / 2), 60);
        d.drawCircle(DirectHit1Level.SQUARE_X_VALUE + (DirectHit1Level.SQUARE_VALUE / 2),
                DirectHit1Level.SQUARE_Y_VALUE - (DirectHit1Level.SQUARE_VALUE / 2), 100);
        d.drawCircle(DirectHit1Level.SQUARE_X_VALUE + (DirectHit1Level.SQUARE_VALUE / 2),
                DirectHit1Level.SQUARE_Y_VALUE - (DirectHit1Level.SQUARE_VALUE / 2), 140);
    }

    @Override
    public void timePassed() {
    }

    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}
