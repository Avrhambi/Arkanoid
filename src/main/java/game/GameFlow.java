package game;

import animation.GameOverScreen;
import animation.KeyPressStoppableAnimation;
import animation.WInScreen;
import biuoop.KeyboardSensor;
import level.LevelInformation;
import animation.AnimationRunner;
import listener.Counter;
import sprite.LivesIndicator;
import sprite.ScoreIndicator;

import java.util.List;
/**
 * The type GameFlow.
 *
 * @author Avraham Bicha
 * @since 2023-06-14
 */
public class GameFlow {

    public static final int STARTING_LIVES = 3;

    private final AnimationRunner animationRunner;
    private final KeyboardSensor keyboardSensor;
    private final ScoreIndicator scoreIndicator;
    private final LivesIndicator livesIndicator;

    /**
     * Constructor.
     *
     * @param animationRunner1 is tha animation.
     * @param keyboardSensor1  is the keyboard sensor.
     */
    public GameFlow(AnimationRunner animationRunner1, KeyboardSensor keyboardSensor1) {
        this.animationRunner = animationRunner1;
        this.keyboardSensor = keyboardSensor1;
        this.scoreIndicator = new ScoreIndicator();
        this.livesIndicator = new LivesIndicator(new Counter(STARTING_LIVES));
    }

    /**
     * @return the score indicator.
     */
    public ScoreIndicator getScoreIndicator() {
        return this.scoreIndicator;
    }

    /**
     * The method will receive the list with the levels and run them.
     *
     * @param levels is the list of levels.
     */
    public void runLevels(List<LevelInformation> levels) {

        // for loop for each level in the list.
        for (LevelInformation levelInfo : levels) {

            // create the level.
            GameLevel level = new GameLevel(levelInfo, this.animationRunner, this.keyboardSensor,
                    this.scoreIndicator, this.livesIndicator);

            // initialize the level.
            level.initialize();

            while (true) {
                TurnResult result = level.run();
                if (result == TurnResult.LEVEL_CLEARED) {
                    break;
                }
                // the turn was lost - one life gone.
                this.livesIndicator.getLives().decrease(1);
                if (this.livesIndicator.getLives().getValue() <= 0) {
                    this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                            KeyboardSensor.SPACE_KEY, new GameOverScreen(this)));
                    return;                         // ends the program - no restart
                }
                /*
                if all the balls went out of the screen they need to be initialize again and the paddle should
                move to the middle of the screen. This method is tacking care of that.
                 */
                level.initializeBallAndPaddle();
            }
        }
        this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                KeyboardSensor.SPACE_KEY, new WInScreen(this)));
    }
}
