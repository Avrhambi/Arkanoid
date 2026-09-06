package level;

import animation.TargetAnimation;
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
 * The type DirectHit1Level.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -06-14
 */
public class DirectHit1Level implements LevelInformation {

    private final String levelName;
    private final int numberOfBalls;
    private final int paddleSpeed;
    private final int paddleWidth;
    private final int paddleHeight;
    private final Sprite background;
    private final List<Block> blockList;
    private List<Velocity> velocityList;


    public static final int NUMBER_OF_BALLS = 1;
    public static final int PADDLE_WIDTH = 80;
    public static final int PADDLE_HEIGHT = 20;
    public static final int PADDLE_SPEED = 8;
    public static final int BALL_VELOCITY = 5;
    public static final int SQUARE_X_VALUE = 400;
    public static final int SQUARE_Y_VALUE = 200;
    public static final int SQUARE_VALUE = 20;

    /**
     * Constructor.
     */
    public DirectHit1Level() {
        this.levelName = "Direct Hit";
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
        Block b = new Block(new Rectangle(new Point(SQUARE_X_VALUE, SQUARE_Y_VALUE), SQUARE_VALUE,
                SQUARE_VALUE), Color.RED);
        blockList1.add(b);
        return blockList1;

    }

    @Override
    public List<Velocity> createVelocity() {
        List<Velocity> velocityList1 = new ArrayList<>();
        Velocity v = new Velocity(0, -BALL_VELOCITY);
        velocityList1.add(v);
        return velocityList1;
    }

    @Override
    public Sprite createBackground() {
        Background background1 = new Background(new Rectangle(new Point(0, GameLevel.SCREEN_HEIGHT),
                GameLevel.SCREEN_WIDTH, GameLevel.SCREEN_HEIGHT), Color.black);
        return new TargetAnimation(background1);
    }

    @Override
    public void setBallVelocity(List<Velocity> velocity) {
        this.velocityList = velocity;
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
}