import animation.AnimationRunner;
import biuoop.GUI;
import game.GameFlow;
import level.DirectHit1Level;
import level.LevelInformation;
import level.WideEasy2Level;
import level.Green3Level;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Ass6Game.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -05-07
 *
 */
public class Ass6Game {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    /**
     * main method for Arkanoid game.
     *
     * @param args are the main arguments.
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Arkanoid game", SCREEN_WIDTH, SCREEN_HEIGHT);
        AnimationRunner animationRunner = new AnimationRunner(gui);
        GameFlow game = new GameFlow(animationRunner, gui.getKeyboardSensor());
        List<LevelInformation> levelInformationalList = new ArrayList<>();

        // reading levels from main arguments.
        for (String arg : args) {
            switch (arg) {
                case "1" -> levelInformationalList.add(new DirectHit1Level());
                case "2" -> levelInformationalList.add(new WideEasy2Level());
                case "3" -> levelInformationalList.add(new Green3Level());
                default -> {
                }
            }
        }
        if (levelInformationalList.isEmpty()) {
            levelInformationalList.add(new DirectHit1Level());
            levelInformationalList.add(new WideEasy2Level());
            levelInformationalList.add(new Green3Level());
//            levelInformationalList.add(new FinalFourLevel());
        }

        // running the game.
        game.runLevels(levelInformationalList);
        gui.close();
    }
}
