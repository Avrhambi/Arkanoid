package animation;

import biuoop.DrawSurface;

/**
 * The frame-loop contract driven by {@code AnimationRunner} at 60 fps.
 * Levels, the countdown, the pause screen and the end screens are all
 * {@code Animation}s.
 *
 * @author Avraham Bicha
 */
public interface Animation {
    /**
     * Renders and updates a single frame of the animation.
     *
     * @param d the surface to draw this frame on
     */
    void doOneFrame(DrawSurface d);

    /**
     * Whether the animation has finished and the runner should stop.
     *
     * @return true when this animation is done, false to keep running
     */
    boolean shouldStop();
}
