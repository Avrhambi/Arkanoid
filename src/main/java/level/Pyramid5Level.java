package level;

import animation.SunAnimation;
import collidable.Block;
import collidable.Velocity;
import game.GameLevel;
import geometry.Point;
import geometry.Rectangle;
import sprite.Background;
import sprite.Sprite;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

/**
 * The type Pyramid5Level.
 *
 * @author Avraham Bicha
 * @since 2026-09-07
 */
public class Pyramid5Level implements LevelInformation {

    private final String levelName;
    private final int numberOfBalls;
    private final int paddleSpeed;
    private final int paddleWidth;
    private final int paddleHeight;
    private final Sprite background;
    private final List<Block> blockList;
    private List<Velocity> velocityList;


    public static final int NUMBER_OF_BALLS = 3;
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 20;
    public static final int PADDLE_SPEED = 8;
    public static final int BLOCK_WIDTH = 50;
    public static final int BLOCK_HEIGHT = 20;
    public static final int BLOCKS_IN_BOTTOM_ROW = 15;
    public static final int NUMBER_OF_ROWS = 7;
    public static final int BALL_SPEED = 6;
    public static final int ANGLE_SPREAD = 30;
    public static final int FIRST_ROW_Y_OFFSET = 200;


    private static Color[] colorsArray = new Color[]{Color.cyan, Color.pink, Color.blue,
            Color.green, Color.yellow, Color.orange, Color.red};

    /**
     * Constructor.
     */
    public Pyramid5Level() {
        this.levelName = "Pyramid";
        this.background = createBackground();
        this.numberOfBalls = NUMBER_OF_BALLS;
        this.blockList = createBlocks();
        this.velocityList = createVelocity();
        this.paddleWidth = PADDLE_WIDTH;
        this.paddleHeight = PADDLE_HEIGHT;
        this.paddleSpeed = PADDLE_SPEED;
    }

    @Override
    public List<Block> createBlocks() {
        List<Block> blockList1 = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            int blocksInRow = BLOCKS_IN_BOTTOM_ROW - (2 * i);
            double startX = (GameLevel.SCREEN_WIDTH - (blocksInRow * BLOCK_WIDTH)) / 2.0;
            for (int j = 0; j < blocksInRow; j++) {
                Block b1 = new Block(new Rectangle(new Point(startX + (BLOCK_WIDTH * j),
                        GameLevel.SCREEN_HEIGHT - FIRST_ROW_Y_OFFSET - (BLOCK_HEIGHT * i)),
                        BLOCK_WIDTH, BLOCK_HEIGHT), colorsArray[i]);
                blockList1.add(b1);
            }
        }
        return blockList1;
    }

    @Override
    public List<Velocity> createVelocity() {
        List<Velocity> velocityList1 = new ArrayList<>();
        double mid = (NUMBER_OF_BALLS - 1) / 2.0;
        for (int i = 0; i < NUMBER_OF_BALLS; i++) {
            Velocity v = Velocity.fromAngleAndSpeed((i - mid) * ANGLE_SPREAD, BALL_SPEED);
            velocityList1.add(v);
        }
        return velocityList1;
    }

    @Override
    public Sprite createBackground() {
        Background background1 = new Background(new Rectangle(new Point(0, GameLevel.SCREEN_HEIGHT),
                GameLevel.SCREEN_WIDTH, GameLevel.SCREEN_HEIGHT), Color.decode("#1a1a40"));
        return new SunAnimation(background1);
    }


    @Override
    public int numberOfBalls() {
        return numberOfBalls;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        return this.velocityList;
    }

    @Override
    public int paddleSpeed() {
        return this.paddleSpeed;
    }

    @Override
    public int paddleWidth() {
        return this.paddleWidth;
    }

    @Override
    public int paddleHeight() {
        return this.paddleHeight;
    }

    @Override
    public String levelName() {
        return this.levelName;
    }

    @Override
    public Sprite getBackground() {
        return this.background;
    }

    @Override
    public List<Block> blocks() {
        return this.blockList;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return this.blockList.size();
    }

    @Override
    public void setBallVelocity(List<Velocity> velocity) {
        this.velocityList = velocity;
    }

}
