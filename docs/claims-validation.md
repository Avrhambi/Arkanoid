# Claims Validation

This document tracks every claim the project makes about itself (in the README
and in code) against what the code actually does. It is the acceptance checklist
for the portfolio-hardening work.

- **Before** = state at commit `d0cf96f` (branch `main`, 2026-09-06), the point
  this effort started.
- **After** = state once the hardening work is merged. Updated as steps land.

Legend: ✅ true / accurate · ❌ false or broken · ⚠️ incomplete, stale, or misleading

| # | Claim (source) | Before | After | Evidence / notes |
|---|---|---|---|---|
| 1 | "implementation of the classic Arkanoid game" (README) | ⚠️ | _pending_ | Game logic is complete, but the project does not build unmodified (see #8). |
| 2 | "Multiple Levels ... selected by command-line arguments or defaults" (README) | ✅ | _pending_ | `ArkanoidGame.java` parses `1/2/3`, defaults to all three. Grows to 5 levels. |
| 3 | "Smooth Animations ... 60 frames per second" (README) | ✅ | _pending_ | `AnimationRunner.FRAMES_PER_SECOND = 60`, frame-time sleep in the loop. |
| 4 | "game flow is managed by a dedicated GameFlow class" (README) | ✅ | _pending_ | `game/GameFlow.java`. |
| 5 | "detects and reacts to keyboard input ... control the paddle" — README says "arrow keys" (README) | ⚠️ | _pending_ | `Paddle` uses `LEFT_KEY`/`RIGHT_KEY` ✅, but `p` (pause) / `space` are undocumented. |
| 6 | "Java SE 10 or higher" / "Java 8 or later" (README) | ❌ | _pending_ | Arrow-`switch` in `main` requires **Java 14+**. Target is now **Java 17**. |
| 7 | Download link → `github.com/Avrhambi/Arkanoid_Game/raw/.../Arkanoid.jar` (README) | ❌ | _pending_ | Repo is `Avrhambi/Arkanoid`; the linked repo does not exist. |
| 8 | "Compile and run the Java classes" — no build instructions (README) | ❌ | _pending_ | No build tool; `biuoop` jar absent from repo; `ArkanoidGame.java` declared `class Ass6Game` → `javac` fails. |
| 9 | `java Ass6Game 1 2 3` (README) | ⚠️ | _pending_ | Class renamed to `ArkanoidGame`; invocation becomes `java -jar target/Arkanoid.jar 1 2 3`. |
| 10 | "Project Structure" section lists `Ass6Game.java`, describes `BuildingAnimation` as an animation (README) | ⚠️ | _pending_ | File is `ArkanoidGame.java`; section omits most packages; `*Animation` classes are static decorator sprites. |
| 11 | Screenshots labelled "level 1 / 2 / 3" (README) | ⚠️ | _pending_ | Hosted on `github.com/user-attachments`; to be re-captured from the current build and committed under `docs/img/`. |
| 12 | Committed `Arkanoid.jar` is the current game (repo) | ❌ | _pending_ | Built 2023-10-23 from a different package layout (`collision/`, `sprites/`, levels `Beginner/Amateur/Pro`). Removed from VCS; CI publishes a fresh artifact. |
| 13 | "Game Over" screen exists / the game can be lost (code) | ❌ | _pending_ | Screen is shown, but the level then silently re-initialises forever — no end state, no lives. Fixed by the lives system. |

## Final acceptance script

Run from a fresh `git clone` of `main`:

```
mvn -B verify                      # compile + checkstyle (non-blocking) + all JUnit tests green
mvn -B package                     # target/Arkanoid.jar (fat jar, biuoop bundled)
java -jar target/Arkanoid.jar      # GUI opens; HUD shows Lives + Score + Level Name
java -jar target/Arkanoid.jar 4 2  # runs only those levels, in that order
#  lose 3 turns    -> Game Over screen; space -> process exits (no restart loop)
#  clear all levels -> You Win screen with final score
```

Plus: the GitHub Actions run on the merge commit is green, and `README.md`
contains no reference to `Ass6Game`, "Java 8", "Java SE 10", or the
`Arkanoid_Game` URL.
