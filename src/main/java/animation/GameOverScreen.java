package animation;

import biuoop.DrawSurface;
import game.GameFlow;
/**
 * The type GameOverScreen.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class GameOverScreen implements Animation {
    private final boolean stop;
    private final GameFlow gameFlow;

    /**
     * Constructor.
     *
     * @param gameFlow is the game flow.
     */
    public GameOverScreen(GameFlow gameFlow) {
        this.stop = false;
        this.gameFlow = gameFlow;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        d.drawText(d.getWidth() / 4, d.getHeight() / 3,
                "Game Over: Your score is " + this.gameFlow.getScoreIndicator()
                        .getScoreTrackingListener().getCounterCurrentScore().getValue(), 32);
        d.drawText(d.getWidth() / 3, d.getHeight() / 3 * 2, "Press space to continue ", 32);
    }
    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}

