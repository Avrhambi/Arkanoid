# CLAUDE.md

## What this is

A desktop clone of the arcade game **Arkanoid** (a ball, a paddle, breakable
blocks) written in Java on top of `biuoop` — a small teaching GUI toolkit
(window, draw surface, keyboard sensor). Originally a university OOP assignment;
being hardened into a portfolio project.

## Run / build

| Command | What it does |
|---|---|
| `mvn -B verify` | compile + checkstyle (non-blocking) + JUnit tests |
| `mvn -B package` | build `target/Arkanoid.jar` (fat jar, biuoop bundled) |
| `java -jar target/Arkanoid.jar` | play all levels |
| `java -jar target/Arkanoid.jar 4 2` | play only the given levels, in order |

Requires **JDK 17+**. `biuoop-1.4.jar` is vendored under `maven-repo/` as an
in-project Maven repository — no manual install step.

Controls: **← →** move paddle · **p** pause · **space** resume / dismiss a screen.

## Key files

- `src/main/java/ArkanoidGame.java` — entry point; parses level args, builds the level list.
- `game/GameFlow.java` — runs the level sequence; owns the shared **score** and **lives** counters.
- `game/GameLevel.java` — one level: the sprite/collidable registries and the per-frame game loop (`Animation`).
- `game/GameEnvironment.java` — collision broker: given a trajectory, returns the closest hit.
- `level/LevelInformation.java` — the extension point. One implementing class per level (`DirectHit1Level`, …).
- `sprite/Ball.java` — movement + collision response (`moveOneStep`).
- `collidable/Paddle.java`, `collidable/Block.java` — the things a ball bounces off.
- `listener/` — `HitListener` observers: `BlockRemover`, `BallRemover`, `ScoreTrackingListener`, `Counter`.
- `geometry/` — pure math: `Point`, `Line`, `Rectangle`. Covered by the unit tests.

## Architecture notes

1. **Sprite vs Collidable.** Everything drawn is a `Sprite` (`drawOn` + `timePassed`).
   Things a ball can hit are also `Collidable` (`getCollisionRectangle` + `hit`).
   `GameLevel` keeps two registries and the game loop walks both each frame.
2. **Collision is trajectory-based, not overlap-based.** Each frame `Ball` builds
   the line from its current point to its next, asks `GameEnvironment` for the
   closest intersection, and either moves freely or snaps to the hit point and
   asks the `Collidable` for a new velocity.
3. **Hits are events.** `Block implements HitNotifier`; on a hit it notifies its
   `HitListener`s. That is how blocks get removed, balls get removed at the
   bottom wall, and the score goes up — no central switchboard.
4. **Levels are data.** `LevelInformation` supplies blocks, ball velocities,
   paddle size/speed, and a background. Adding a level = one new class + one
   line in `ArkanoidGame`. Nothing else changes.
5. **Animation is one interface.** `Animation` = `doOneFrame` + `shouldStop`;
   `AnimationRunner` drives any of them at 60 fps. Levels, countdown, pause, and
   the end screens are all `Animation`s.

## Conventions / gotchas

- **`geometry/` uses an upward-Y convention** for rectangles: a rectangle's
  lower-left is `upperLeft.y - height`. Consistent everywhere; do not "fix" it.
- `Ball.moveOneStep` and `Line`'s intersection math are load-bearing, untested
  legacy, and epsilon-sensitive. Change only with characterization tests first.
- `Point.equals(Point)` / `Line.equals(Line)` are intentional non-`Object`
  overrides (float-tolerant, mutable `Point`). Missing `hashCode` is known and
  accepted; checkstyle does not fail the build on it.
- Package layout is flat under `src/main/java` (no top-level namespace).

## Project knowledge

- Claims tracker: `docs/claims-validation.md` — every README/code claim vs. reality.
- Architecture map: `docs/architecture/index.html` (via the `architecture-doc` skill), once it exists. Consult before answering "explain / how does X work"; check its `reviewed-at` against `git log` first.
- Session handoff: `/handoff` before `/compact` or `/clear`; `/handoff resume` to reload.
