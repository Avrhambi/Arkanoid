package sprite;

import biuoop.DrawSurface;
import game.GameLevel;
import level.LevelInformation;

import java.awt.Color;
/**
 * The type LevelIndicator.
 * present the level of the game on the screen
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class LevelIndicator implements Sprite {

    private final LevelInformation levelInformation;

    public static final int Y_FOR_LEVEL_NAME = 15;
    public static final int X_FOR_LEVEL_NAME = 600;
    public static final int FONT_SIZE = 15;

    /**
     * Constructor.
     *
     * @param levelInformation1 is the level information.
     */
    public LevelIndicator(LevelInformation levelInformation1) {
        this.levelInformation = levelInformation1;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.black);
        d.drawText(X_FOR_LEVEL_NAME, Y_FOR_LEVEL_NAME,
                "Level Name: " + this.levelInformation.levelName(), FONT_SIZE);
    }

    @Override
    public void timePassed() {
    }

    /**
     * add the level indicator to the sprite list.
     *
     * @param game is the game.
     */
    public void addToGame(GameLevel game) {
        game.addSprite(this);
    }
}

