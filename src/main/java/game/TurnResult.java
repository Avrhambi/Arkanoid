package game;

/**
 * The outcome of a single turn of a {@link GameLevel}.
 *
 * <p>{@link #LEVEL_CLEARED} means every block that had to be removed was
 * removed; {@link #TURN_LOST} means every ball fell past the paddle.
 *
 * @author Avraham Bicha
 * @since 2026-09-06
 */
public enum TurnResult {

    /**
     * The level was cleared - all required blocks were removed.
     */
    LEVEL_CLEARED,

    /**
     * The turn was lost - all balls fell past the paddle.
     */
    TURN_LOST
}
