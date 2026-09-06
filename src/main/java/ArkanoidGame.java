import animation.AnimationRunner;
import biuoop.GUI;
import game.GameFlow;
import game.GameLevel;
import level.DirectHit1Level;
import level.LevelInformation;
import level.WideEasy2Level;
import level.Green3Level;

import java.util.ArrayList;
import java.util.List;

/**
 * The type ArkanoidGame.
 *
 * @author Avraham Bicha
 * @since 2023-05-07
 *
 */
public class ArkanoidGame {

    /**
     * main method for Arkanoid game.
     *
     * @param args are the main arguments.
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Arkanoid game", GameLevel.SCREEN_WIDTH, GameLevel.SCREEN_HEIGHT);
        AnimationRunner animationRunner = new AnimationRunner(gui);
        GameFlow game = new GameFlow(animationRunner, gui.getKeyboardSensor());
        List<LevelInformation> levelInformationalList = new ArrayList<>();

        // reading levels from main arguments; unrecognised args are skipped.
        for (String arg : args) {
            if ("1".equals(arg)) {
                levelInformationalList.add(new DirectHit1Level());
            } else if ("2".equals(arg)) {
                levelInformationalList.add(new WideEasy2Level());
            } else if ("3".equals(arg)) {
                levelInformationalList.add(new Green3Level());
            }
        }
        if (levelInformationalList.isEmpty()) {
            levelInformationalList.add(new DirectHit1Level());
            levelInformationalList.add(new WideEasy2Level());
            levelInformationalList.add(new Green3Level());
        }

        // running the game.
        game.runLevels(levelInformationalList);
        gui.close();
    }
}
