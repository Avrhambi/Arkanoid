# Two more levels

## Why

The README advertises "Multiple Levels" and a commented-out
`// levelInformationalList.add(new FinalFourLevel())` in `ArkanoidGame` shows a
fourth was always intended. Three levels is thin for a portfolio piece whose
headline design point is *"adding a level = one new class + one line"*
(`CLAUDE.md`). Shipping levels 4 and 5 demonstrates that the `LevelInformation`
extension point actually holds up.

## Who it's for

The interviewer who reads `CLAUDE.md`'s claim that levels are pure data and then
checks whether adding one really is that cheap — and the player who runs the
jar with no arguments and gets a game with a real progression.

## Success criteria

- `java -jar target/Arkanoid.jar` plays levels 1 → 2 → 3 → 4 → 5 in order.
- `java -jar target/Arkanoid.jar 4` plays only level 4; `... 5 4` plays 5 then 4.
- Each new level is one class implementing `LevelInformation`, on the exact
  pattern of `Green3Level` (final fields set in the ctor, `numberOfBlocksToRemove`
  returns `blockList.size()`, `createVelocity` builds exactly `numberOfBalls`
  entries).
- Both new levels are winnable and lose-able; blocks sit below the HUD/top frame
  and inside the side frames.
- `mvn verify` stays green with 0 checkstyle violations; the 30 existing tests
  are untouched.

## Hard constraints

- No change to `Ball`, `Line`, `Rectangle`, `GameLevel`, `GameFlow`, or any
  `*Animation` class. New level classes + the `ArkanoidGame` wiring only.
- Reuse the existing `Background` + `BuildingAnimation` / `SunAnimation` /
  `TargetAnimation` decorators for backgrounds — do not write a new animation.
- `createVelocity()` must return a list of length exactly `numberOfBalls()` on
  both the constructor path and the `setBallVelocity(createVelocity())` reset
  path (Step 6 QA flagged that `GameLevel.createBalls()` now indexes
  `initialBallVelocities().get(i)` without a catch).

## Design

### Level 4 — `FinalFour4Level` (`levelName() == "Final Four"`)

The classic BIU level 4.

- **Blocks:** 7 rows, 15 blocks per row, one colour per row (cycle through
  `Color.gray, red, yellow, green, white, pink, cyan`). Block 50×20. Rows filled
  right-to-left from `SCREEN_WIDTH - SCREEN_FRAME_SIZE`, same idiom as
  `Green3Level.createBlocks()`. First row near `y = SCREEN_HEIGHT - 250`
  (mirror Green3's use of `GameLevel` constants; pick a start Y that leaves the
  HUD clear and stacks 7 rows downward without hitting the paddle zone).
- **Balls:** 3, fanned out — `Velocity.fromAngleAndSpeed` with angles spread
  around vertical (e.g. `-45, 0, 45` offset from straight up), speed ~5–6, on
  the `Green3Level.createVelocity()` pattern (`Math.pow(-1, ...)` fan is fine).
- **Paddle:** width 80, height 20, speed ~8 (a touch faster than level 3 —
  105 blocks with 3 balls needs a responsive paddle).
- **Background:** dark, `SunAnimation` or `BuildingAnimation` — pick whichever
  isn't level 2's/3's for visual variety; a plain dark `Background` is also OK.

### Level 5 — pick a distinct shape (`levelName()` your choice, e.g. "Pyramid")

Something visually different from the four flat-grid levels so the progression
reads as designed, not padded. Options (dealer's choice, keep it simple):

- **Pyramid:** row *i* has `15 - 2*i` blocks, centred, for i = 0..6 — a
  triangle. ~57 blocks.
- **Checkerboard:** a grid with every other cell empty.
- **Two towers:** two tall narrow columns of blocks on the left and right.

Constraints for level 5: 2–4 balls, paddle width 80–120, all blocks reachable
(nothing tucked behind a frame), winnable in a reasonable time. Reuse a
background decorator.

### Wiring — `ArkanoidGame`

The arg handling is now an `if / else if` chain (Step 6). Add:

```java
} else if ("4".equals(arg)) {
    levelInformationalList.add(new FinalFour4Level());
} else if ("5".equals(arg)) {
    levelInformationalList.add(new <Level5Class>());
}
```

and append `new FinalFour4Level()` + the level-5 class to the default list
(the `if (levelInformationalList.isEmpty())` block), after `Green3Level`.

## Commits (atomic)

1. `level: add Final Four (level 4)` — the class + its wiring in `ArkanoidGame`
   (arg `"4"` + default list). Builds and is playable on its own.
2. `level: add <name> (level 5)` — the class + arg `"5"` + default list.

Each commit: `mvn -o -ntp verify` green, jar launches with the new arg.

## After

Run the `security-review` skill over the arg-parsing boundary in `ArkanoidGame`
(expected clean — it's a fixed `if/else` on string literals, no reflection, no
file/network I/O). Update `docs/claims-validation.md` row 2 ("Multiple Levels")
to reflect 5 levels.
