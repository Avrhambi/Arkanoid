# Lives system & real Game Over

## Why

The game as shipped **cannot be lost**. `GameFlow.runLevels` loops `while (true)`
(`game/GameFlow.java:64`); when every ball in a turn falls past the paddle it
shows `GameOverScreen` for one key-press, calls `level.initializeBallAndPaddle()`,
and loops again — forever. "Game Over" is a screen with no consequence. There is
no lives counter and no HUD element for one. A commented-out `// this.lives = lives;`
(`game/GameLevel.java:84`) shows the feature was started and abandoned; the
leftover `Counter implements Sprite` with dead `X_FOR_LIFE`/`Y_FOR_LIFE`
constants (`listener/Counter.java:15,19-21`) is the other half of that stub.

For a portfolio piece this is the single most visible correctness gap: a
reviewer who plays it discovers the game never ends.

## Who it's for

Anyone running the finished jar — and the interviewer reading the diff, who
sees a real bug found and fixed with a minimal, well-scoped change that reuses
the existing `ScoreIndicator` ownership pattern rather than inventing a new one.

## Success criteria

- Start with **3 lives**, shared across all levels (like the existing score).
- A lost turn (all balls gone) costs **exactly one** life, then the turn
  restarts with the countdown.
- HUD shows `Lives: N` alongside `Score` and the level name.
- Lives reach 0 → `GameOverScreen`, one space-press, **program exits**. No
  restart, no infinite loop.
- Clearing the final level with lives > 0 → `WInScreen` (unchanged).

## Hard constraints

- **No change** to `Ball.moveOneStep`, `Line`, or any collision math (fenced —
  see `CLAUDE.md` "Conventions / gotchas").
- Mirror the existing `ScoreIndicator` ownership pattern exactly.
- The per-turn `remainingBalls` counter in `GameLevel` (re-`increase`d by
  `createBalls()` every turn) is a **different thing** from lives and must not
  be conflated.

## Design

### Shared lives counter — owned by `GameFlow`

`GameFlow` already `new`s a `ScoreIndicator` in its constructor
(`GameFlow.java:34`) and threads it into every `GameLevel`. Add the same for
lives:

- `GameFlow` constant `public static final int STARTING_LIVES = 3;`
- field `private final LivesIndicator livesIndicator;`, initialised in the
  constructor as `new LivesIndicator(new Counter(STARTING_LIVES))`.
- getter `getLivesIndicator()` parallel to `getScoreIndicator()`.

### `sprite/LivesIndicator.java`

A near-copy of `sprite/ScoreIndicator.java`:

- ctor `LivesIndicator(Counter lives)` — stores the `Counter` (does **not**
  create it; `GameFlow` owns the number).
- `getLives()` → `Counter`
- `addToGame(GameLevel game)` → `game.addSprite(this)`
- `drawOn(d)` → `d.setColor(Color.black);
  d.drawText(X_FOR_LIVES, Y_FOR_LIVES, "Lives: " + lives.getValue(), FONT_SIZE);`
  with `Y_FOR_LIVES = 15`, `FONT_SIZE = 15`, `X_FOR_LIVES = 100` (Score sits at
  `SCREEN_WIDTH/2 - 10 = 390`, the level name further right — 100 is clear of
  both).
- `timePassed()` empty (Sprite contract).

### Turn-outcome signal — replace the `num` sentinel

`GameLevel.run()` is `void` (`GameLevel.java:255-261`); `GameFlow` currently
re-derives the outcome from block-counter arithmetic with a fragile `num`
sentinel (`GameFlow.java:62-68`). Replace with an explicit result:

- new enum `game/TurnResult` — `LEVEL_CLEARED`, `TURN_LOST`.
- `GameLevel` field `private boolean cleared;`
  - reset to `false` at the **top** of `run()`, before the countdown.
  - set to `true` inside the existing blocks-cleared `if` at
    `GameLevel.java:300-304` (the branch that already does `running = false` and
    the `+100` score bump). **Not** in the balls-gone branch at `:307-309`.
- `run()` signature `void` → `TurnResult`; after `this.runner.run(this)` returns:
  `return this.cleared ? TurnResult.LEVEL_CLEARED : TurnResult.TURN_LOST;`
  If both branches fire on the same frame, `LEVEL_CLEARED` wins (only `cleared`
  is checked).
- `run()` is **not** part of the `Animation` interface (`doOneFrame` +
  `shouldStop`), so this signature change is contained to the one caller.

### `GameFlow.runLevels` rewrite

```java
for (LevelInformation levelInfo : levels) {
    GameLevel level = new GameLevel(levelInfo, animationRunner, keyboardSensor,
                                    scoreIndicator, livesIndicator);
    level.initialize();
    while (true) {
        TurnResult result = level.run();
        if (result == TurnResult.LEVEL_CLEARED) {
            break;
        }
        livesIndicator.getLives().decrease(1);
        if (livesIndicator.getLives().getValue() <= 0) {
            animationRunner.run(new KeyPressStoppableAnimation(keyboardSensor,
                    KeyboardSensor.SPACE_KEY, new GameOverScreen(this)));
            return;                         // ends the program — no restart
        }
        level.initializeBallAndPaddle();
    }
}
animationRunner.run(new KeyPressStoppableAnimation(keyboardSensor,
        KeyboardSensor.SPACE_KEY, new WInScreen(this)));
```

Behaviour change: `GameOverScreen` now appears **only at 0 lives**, not on every
lost turn. A lost turn with lives left goes straight to
`initializeBallAndPaddle()` + `run()` (whose `CountdownAnimation` gives the
player the "get ready" beat).

### `GameLevel` constructor

Add `LivesIndicator livesIndicator` as the 5th parameter (fulfilling the
abandoned `// this.lives = lives;` at `:84` — delete that comment). Store in
`private final LivesIndicator livesIndicator;`. In `initialize()` (`:216`), add
`this.livesIndicator.addToGame(this);` next to `this.scoreIndicator.addToGame(this)`.
**Not** in `initializeBallAndPaddle()` — one persistent HUD sprite.

> `initializeBallAndPaddle()` already re-adds the score sprite and news a fresh
> `LevelIndicator` every turn — a pre-existing minor duplication. Do **not**
> copy that pattern for lives, and do **not** fix the existing dup here (Step 6).

### `Counter` → plain int holder (folded in — same feature)

`listener/Counter.java` `implements Sprite` with empty `drawOn`/`timePassed`,
an `addToGame`, and the `X_FOR_LIFE`/`Y_FOR_LIFE`/`FONT_SIZE` constants — all
vestiges of this same abandoned lives work. Verified never used as a Sprite
(no `Counter` is ever passed to `addSprite`; the three constants have zero
references). Drop `implements Sprite`, the three imports
(`biuoop.DrawSurface`, `game.GameLevel`, `sprite.Sprite`), the two Sprite
methods, `addToGame`, and the three constants. Result is exactly the int holder
that `CounterTest` (Step 4) already pins.

## The `remainingBalls` trap (QA's named check)

`ballRemover`'s `Counter` is **per-`GameLevel`**. `createBalls()` does
`remainingBalls.increase(1)` per ball on every (re)init; `BallRemover.hitEvent`
is the only decrement and fires only for the bottom-wall block. So it self-heals
to `numberOfBalls()` on each `initializeBallAndPaddle()` with no explicit reset.
The lives `Counter` is a **distinct object owned by `GameFlow`**, decremented
once per `TURN_LOST`, never touched by `createBalls()`. They do not interact.

## Commits (atomic)

1. `listener: reduce Counter to a plain int holder` — drop the `Sprite` role and
   the dead lives-HUD constants. Build + `CounterTest` still green on their own.
2. `game: add lives system with real Game Over` — `TurnResult`, `LivesIndicator`,
   `GameLevel.run()` returns `TurnResult`, `GameLevel` ctor gains the param,
   `GameFlow` owns the shared lives `Counter` and its loop ends at 0 lives.
   (Commits 2 can't be split further — `run()`'s new return type is dead code
   until `GameFlow` consumes it.)
