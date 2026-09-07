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
| 1 | "implementation of the classic Arkanoid game" (README) | ⚠️ | ✅ | `mvn verify` builds from a clean clone; `java -jar target/Arkanoid.jar <n>` runs each of the five levels (screenshots in `docs/img/`). Single-level clear → You Win, three lost turns → Game Over → process exits, all confirmed by playthrough. A full 1→5 sequential run is not separately evidenced. |
| 2 | "Multiple Levels ... selected by command-line arguments or defaults" (README) | ✅ | ✅ | `ArkanoidGame.java` parses `1`–`5` (unknown args skipped); an empty selection defaults to all five levels in order. |
| 3 | "Smooth Animations ... 60 frames per second" (README) | ✅ | ✅ | `AnimationRunner.FRAMES_PER_SECOND = 60`, frame-time sleep in `run()`. |
| 4 | "game flow is managed by a dedicated GameFlow class" (README) | ✅ | ✅ | `game/GameFlow.java` — owns the score + lives counters and the level sequence. |
| 5 | "detects and reacts to keyboard input ... control the paddle" — README says "arrow keys" (README) | ⚠️ | ✅ | `Paddle` uses `LEFT_KEY`/`RIGHT_KEY`; `p` (pause) and `space` (resume/dismiss) are now documented in the README Controls line. |
| 6 | "Java SE 10 or higher" / "Java 8 or later" (README) | ❌ | ✅ | README states **JDK 17+**; `pom.xml` pins `maven.compiler.release=17`; CI configured for Temurin 17. Verified: fresh-clone `./mvnw verify` compiles at `release 17`. |
| 7 | Download link → `github.com/Avrhambi/Arkanoid_Game/raw/.../Arkanoid.jar` (README) | ❌ | ✅ | Broken link removed. README quickstart is `git clone` + `./mvnw package`; CI configured to upload the jar as a build artifact (pending the billing unlock — see CI status below). |
| 8 | "Compile and run the Java classes" — no build instructions (README) | ❌ | ✅ | Maven build; `biuoop` vendored under `maven-repo/`; class renamed `Ass6Game` → `ArkanoidGame`. `./mvnw verify` green from an empty local repo. |
| 9 | `java Ass6Game 1 2 3` (README) | ⚠️ | ✅ | README now `java -jar target/Arkanoid.jar 1 2 3`; manifest `Main-Class: ArkanoidGame`. |
| 10 | "Project Structure" section lists `Ass6Game.java`, describes `BuildingAnimation` as an animation (README) | ⚠️ | ✅ | Rewritten as "Project layout" — every package with a one-line role; `*Animation` classes described as background decorators / screens. |
| 11 | Screenshots labelled "level 1 / 2 / 3" (README) | ⚠️ | ✅ | Re-captured from the current build, committed under `docs/img/`, referenced by relative path. Covers all 5 levels + countdown + Game Over + You Win. |
| 12 | Committed `Arkanoid.jar` is the current game (repo) | ❌ | ✅ | Stale 2023 jar removed from VCS (Step 1); `target/` git-ignored; CI publishes a fresh artifact per run. |
| 13 | "Game Over" screen exists / the game can be lost (code) | ❌ | ✅ | Lives system: `GameFlow` shared lives `Counter` (start 3), `GameLevel.run()` → `TurnResult`, `runLevels` exits at 0 lives. Confirmed by playthrough — Game Over screen reached at score 90, no restart loop. |

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

Plus: `README.md` contains no reference to `Ass6Game`, "Java 8", "Java SE 10",
or the `Arkanoid_Game` URL.

**CI status.** `main` is pushed (`origin/main` = `69c87aa`). The push triggered
the CI workflow, but the run did not start: *"The job was not started because
your account is locked due to a billing issue"* — an account-level GitHub lock,
not a repo or workflow problem (the repo is public; Actions is enabled;
`allowed_actions: all`).

The `./mvnw -B -ntp verify` command itself is verified green from a fresh clone
with an empty `~/.m2` (wrapper downloads Maven 3.9.9, SHA-256 checked; all
plugins and `biuoop` resolve cold; 30 tests pass; 0 checkstyle violations).
Once the billing lock is cleared, re-run the workflow (`gh run rerun` or an
empty commit) to get the green badge — that is the only remaining acceptance
item and it is outside the codebase.
