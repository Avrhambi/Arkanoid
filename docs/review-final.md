# Final adversarial review — `d0cf96f..main`

Reviewer pass over the whole portfolio-hardening diff (9 merged steps).
Date: 2026-09-07. Classification: **BLOCKER** / **SHOULD-FIX** / **NIT**.

---

## 1. Verdict

The working tree is portfolio-quality: the build is green (including a
verified *cold* dependency resolution from the in-project `maven-repo/`), all 30
characterization tests pass, checkstyle is at 0, the fat jar is runnable, and
the two headline pieces of new work — the lives / real Game Over system and the
two new levels — are **correct** on close inspection (no off-by-one in the turn
loop, the `cleared` flag is timed right, per-turn `remainingBalls` and per-run
`lives` are genuinely separate objects, both new levels place all blocks
on-screen with velocity lists matching `numberOfBalls()`).

It is **not ship-ready yet, for one reason that is outside the diff:** none of
this work has been pushed. `origin/main` is still at `d0cf96f`, GitHub Actions
has never run, and `.github/workflows/ci.yml` 404s on the remote — so a fresh
`git clone` of the published repo today gets the pre-work commit, which per
Step 2's own notes *does not compile* and has no `pom.xml`. Every CI claim in
the README and the claims tracker is currently true only locally. Push `main`
and confirm the Actions run is green, and the verdict flips to ship-ready.

---

## 2. Build & tests (run by the reviewer)

### 2.1  Standard verify (warm local repo)

```
JAVA_HOME=/c/Program Files/Eclipse Adoptium/jdk-21.0.6.7-hotspot
"$MVN" -o -ntp verify
```

| Result | Value |
|---|---|
| BUILD | **SUCCESS** (~12 s) |
| Tests | **30 run, 0 failures, 0 errors, 0 skipped** — VelocityTest 5, LineTest 10, PointTest 5, RectangleTest 5, CounterTest 5 |
| Checkstyle | **0 violations** (`config/checkstyle/biuoop.xml`, bound to `verify`, `failOnViolation=false`) |
| Package | `target/Arkanoid.jar` — shaded, `Main-Class: ArkanoidGame`, `biuoop/*` + `level/FinalFour4Level` + `level/Pyramid5Level` present |
| Surefire | `-Djava.awt.headless=true` (matches README) |

### 2.2  Cold dependency resolution (the self-contained-build claim)

`~/.m2/repository` already held `ac.biu.oop:biuoop:1.4`, so §2.1 does **not**
prove `maven-repo/` works. Re-tested with that artefact removed from `~/.m2`:

* `"$MVN" -ntp -B clean verify` (online, CI-equivalent) → **BUILD SUCCESS**,
  `biuoop` pulled from `file://…/maven-repo`, 30 tests green. **The vendoring
  works for the CI path.**
* `"$MVN" -o -ntp verify` (offline) → **BUILD FAILURE**: *"Cannot access
  project-local (file://…/maven-repo) in offline mode and the artifact
  ac.biu.oop:biuoop:jar:1.4 has not been downloaded from it before."* Maven's
  `-o` flag blocks a `file://` repo it has not previously cached. See §6 NIT —
  the documented acceptance script (`mvn -B verify`, no `-o`) is unaffected.

### 2.3  CI — never executed

`gh api repos/Avrhambi/Arkanoid/actions/runs` → `total_count: 0`.
`gh api …/contents/.github/workflows/ci.yml?ref=main` → **404**.
`git ls-remote origin main` → `d0cf96f…` (the pre-work commit). See §6 BLOCKER.

---

## 3. Correctness findings

### 3.1  Lives-system turn loop — CORRECT (no finding)

`GameFlow.runLevels` (`src/main/java/game/GameFlow.java:69-90`):

```
while (true) {
    TurnResult result = level.run();
    if (result == TurnResult.LEVEL_CLEARED) break;
    this.livesIndicator.getLives().decrease(1);
    if (this.livesIndicator.getLives().getValue() <= 0) { GameOverScreen; return; }
    level.initializeBallAndPaddle();
}
```

Start `Counter(3)`. Trace: TURN_LOST → 2, TURN_LOST → 1, TURN_LOST → 0 → `<= 0`
→ Game Over → `return`. Exactly **3 turns**; no off-by-one. `main` then reaches
`gui.close()` and the process exits.

### 3.2  `cleared` flag timing — CORRECT (no finding)

Reset `false` at the top of `run()` before the countdown
(`GameLevel.java:246`); set `true` only in the blocks-cleared branch of
`doOneFrame` (`GameLevel.java:293-297`), which also sets `running = false`.
`AnimationRunner.run` is `while (!shouldStop()) { doOneFrame; … }`, so the
clearing frame runs once and `run()` returns
`cleared ? LEVEL_CLEARED : TURN_LOST`. Last-block + last-ball on the same frame
→ block branch first → `LEVEL_CLEARED` wins (player-favourable). `ADD_IN_WIN`
(+100) added exactly once.

### 3.3  `remainingBalls` vs `lives` not conflated — CONFIRMED

`ballRemover` holds a `new Counter()` per `GameLevel` (`GameLevel.java:78`);
`createBalls()` `increase(1)`s it per ball, `BallRemover.hitEvent` is the only
decrement. Self-heals 0 → `numberOfBalls()` on each `initializeBallAndPaddle()`.
The lives `Counter(3)` is a distinct object in `GameFlow` (`GameFlow.java:39`),
`decrease(1)` once per `TURN_LOST`, never touched by `createBalls()`.

### 3.4  New levels — no off-screen blocks, velocity counts correct

Playfield after `initializeFrame`: x ∈ [25, 775]; usable y roughly (50, 555)
(top frame spans screen y [25, 50]; `bot` at anchor y 625 spans [600, 625],
i.e. just below the visible area; paddle at y ≈ 555–575; ball spawn y ≈ 525).

* **FinalFour4Level** — 7 rows × 15 = **105 blocks**. `x = 800 - 25 - 50*j`,
  j = 1..15 → block left edges [25, 725], right edge of j=1 at 775 (flush with
  the wall, not overlapping). `y = 600 - 200 - 20*i`, i = 0..6 → block rows
  occupy screen y ∈ **[260, 400]** — clear of the HUD (≤ 50), the side frames,
  and the paddle. `createVelocity()` returns exactly `NUMBER_OF_BALLS` (3);
  angles `(i-1)*30` → −30 / 0 / +30 off vertical, all with an upward component
  (no stuck horizontal ball). `numberOfBlocksToRemove()` = `blockList.size()` →
  `doOneFrame`'s `num` = 0, clears at `remainingBlocks == 0`.
* **Pyramid5Level** — rows 15,13,11,9,7,5,3 = **63 blocks**. `startX =
  (800 - blocksInRow*50)/2` → i=0 x ∈ [25, 775], narrower rows centred inside.
  Same y ∈ [260, 400]. `createVelocity()` → 3 entries. `SunAnimation` background
  reused per the design constraint. `colorsArray` has exactly 7 entries for
  7 rows.

No `IndexOutOfBoundsException` risk in `createBalls()` /
`initializeBallAndPaddle()`: both new levels return `initialBallVelocities()`
lists of length `numberOfBalls()` on both the constructor and the
`setBallVelocity(createVelocity())` reset paths.

### 3.5  Step 4's two "locked quirks" — descriptions ACCURATE

* **collinear overlap → null** — `Line(0,0,10,0).intersectionWith(Line(5,0,15,0))`
  returns `null`: `isIntersectionWithLines` hits `Math.abs(m1 - m2) <= epsilon`
  (both slopes 0) → `checkCases(other)` → no endpoint of `a` equals an endpoint
  of `b` → `null`. NOTE text correct.
* **`Rectangle.isPointInList` inverted** — `Rectangle.java:38-47` ends
  `return !isOk`: `true` when the point is **absent**. The method's own Javadoc
  claims the opposite; callers (`Rectangle.java:71,77,83`) use it correctly as a
  "not already present" dedup guard. NOTE text correct.

### 3.6  Paddle registered twice — real, but PRE-EXISTING (NIT)

`GameLevel` calls `paddle.addToGame(this)` in the constructor
(`GameLevel.java:87`) **and** `this.paddle.addToGame(this)` in `initialize()`
(`GameLevel.java:214`). `addToGame` = `addSprite` + `addCollidable`, no dedup,
so `SpriteCollection.notifyAllTimePassed()` invokes `Paddle.timePassed()` twice
per frame.

Concrete effect: hold `→` in level 4 and the paddle advances `2 * PADDLE_SPEED`
= 16 px/frame though `FinalFour4Level.PADDLE_SPEED = 8`; it is also drawn twice.

Both `addToGame` calls exist unchanged at `d0cf96f` — not introduced by this
work, and every level's `paddleSpeed()` was presumably tuned with the doubling
in effect. Flagged because `/simplify` should drop the constructor call, and
because it is a genuine latent bug.

### 3.7  `initializeBallAndPaddle` re-adds HUD sprites each turn — PRE-EXISTING (NIT)

`GameLevel.initializeBallAndPaddle()` (`GameLevel.java:224-238`) re-adds
`scoreIndicator` and news+adds a fresh `LevelIndicator` every lost turn. After
losing 2 turns on one level, 3 `ScoreIndicator` sprites draw identical text at
the same spot. Bounded (≤ 3 before Game Over), invisible, no state impact. The
design note acknowledges it and correctly does not copy the pattern for
`livesIndicator` (added once in `initialize()`, persists).

---

## 4. Simplicity / dead code

| Item | Severity | Note |
|---|---|---|
| `GameLevel.getBlockRemover()` / `getBallRemover()` (`GameLevel.java:97,104`) | NIT | Zero callers (verified). Pre-existing, **deferred** per the brief. |
| `GameFlow.getLivesIndicator()` (`GameFlow.java:52`) | NIT | **New in Step 5.** Zero callers — `GameOverScreen` / `WInScreen` use only `getScoreIndicator()`. Symmetric with it, but dead on arrival. |
| Redundant constructor `paddle.addToGame` | NIT | `initialize()` already does it; removing the constructor call fixes §3.6. |
| Double frame-pacing | NIT | `GameLevel.doOneFrame` sleeps to a ~16 ms budget **and** `AnimationRunner.run` sleeps to a ~16 ms budget around the same call — the outer sleep is effectively dead. Pre-existing. |
| `Counter` reduction (Step 5) | good | Cleanly reduced to a plain int holder; dead constants / `Sprite` role / `biuoop` import all gone; `CounterTest` pins it. |
| `BlockRemover` self-removal (Step 6) | good | `removeHitListener(this)` replaces the `getHitListeners().get(indexOf(this))` dance and is strictly safer; `Block.notifyHit` iterates a copy. |

---

## 5. Claim accuracy

Diagram / prose method names in the README (`getClosestCollision(Line trajectory)`,
`getCollisionRectangle()`, `Block.hit`, `TurnResult`, `cleared`) all exist
verbatim in the code — no phantom methods.

### `docs/claims-validation.md` (spot-check)

| # | Claim | Verdict |
|---|---|---|
| 1 | "plays five levels. Verified by playthrough (screenshots in `docs/img/`)" | **OVERSTATED — SHOULD-FIX.** `ADD_IN_WIN = 100` per level and `WInScreen` fires only after every level clears, so the recorded numbers ("Game Over at 90", "You Win at 105") are consistent with a *single* level run (`java -jar … 1`: 100 clear bonus + 5 block points), not a five-level progression (≥ 500). Per-level screenshots exist; an end-to-end five-level run — and the lives counter carrying across a level boundary — is not evidenced. Reword to "each level verified by screenshot" or do one full run. |
| 2 | parses `1`–`5`, unknown skipped, empty → all five in order | accurate — `ArkanoidGame.java` if/else on `"1"`..`"5"`, `default` list has all five |
| 6 | JDK 17+, `pom.xml` pins `release=17`, CI on Temurin 17 | accurate *as config* — `pom.xml:14`, `ci.yml` `java-version: "17"` / `temurin`. But "CI builds on Temurin 17" has never actually happened (§2.3). |
| 7 | broken download link removed; CI uploads jar | accurate *as config* — `ci.yml` uploads `target/Arkanoid.jar`, `if-no-files-found: error`. No run has produced an artifact yet (§2.3). |
| 13 | lives system; `runLevels` exits at 0 lives; no restart loop | accurate (see §3.1) |

The tracker's own closing line — *"the GitHub Actions run on the merge commit is
green"* — is currently **unsatisfiable**: there is no merge commit on the remote
and no Actions run (§2.3, §6).

### `README.md` (spot-check)

| Claim | Verdict |
|---|---|
| "30 characterization tests" / "0 violations" | accurate |
| "`notifyHit` iterates a copy of the listener list" | accurate — `Block.java:161` |
| "~20 KB of binary" (vendored jar) | accurate — 20,169 bytes |
| **"never tunnels through a block at speed and never resolves a false overlap"** (opening paragraph) | **OVERSTATED — SHOULD-FIX.** The README itself later describes `Ball.moveOneStep` as having "an escape-the-block `while` loop" (a mechanism that only exists because the ball *does* sometimes end up inside a block), and the verified collinear-overlap quirk (§3.5) is a case where the broker misses an intersection that exists. The absolute "never" is contradicted by the document's own later admissions — and this is the sentence stating the project's *defining engineering property*. Fix: describe the mechanism without the absolutes ("a fast ball is tested against the blocks along its path, not only where it lands"). |
| **"No `while (true)`."** (`README.md:157-158`, "Real end state") | **INACCURATE — SHOULD-FIX.** `GameFlow.runLevels` still opens its inner loop with `while (true) {` at `src/main/java/game/GameFlow.java:73`. Behaviour is fine (terminates via `break`/`return`), but the literal claim is contradicted by the one file the "real end state" story rests on. Note the process gap: Step 9 both rewrote the README *and* marked all 13 tracker rows resolved, yet the rewrite introduced this new unverified claim and the tracker pass did not catch it. Fix: reword ("no *unconditional* restart") or hoist the guard to `while (livesIndicator.getLives().getValue() > 0)`. |
| **"`Ball.moveOneStep` … and `Line`'s intersection math … Step 4 added characterization tests that pin their current behaviour"** (`README.md:127-133`) | **PARTIALLY INACCURATE — SHOULD-FIX.** `Line` is pinned (`LineTest`, 10 tests). `Ball.moveOneStep` has **no** test — only its dependencies (`Point`, `Line`, `Rectangle`, `Velocity`). Narrow the sentence to the geometry `moveOneStep` is built from. |
| "`geometry/` … pure math, **fully unit-tested**" (Project layout) | **OVERSTATED — NIT.** `Line.setStartPoint` and several `Point`/`Rectangle` paths have no test; the tests are characterization, not proof of correctness (as the project's own trade-offs section is careful to say). "covered by characterization tests" is the accurate phrasing. |
| "Java 17 (current LTS)" (`README.md:110`) | **STALE — NIT.** As of 2026, Java 21 (and 25, Sep 2025) are the current LTS releases; 17 is a previous LTS. Fix: "Java 17 (LTS)". |

---

## 6. Ship blockers (ranked)

1. **BLOCKER — the work is unpublished.** `origin/main` = `d0cf96f`; 0 GitHub
   Actions runs ever; `.github/workflows/ci.yml` 404s on the remote. A fresh
   clone of the public repo gets the pre-work commit, which does not compile and
   has no `pom.xml` — the README's own `git clone … && ./mvnw verify` quickstart
   fails against what is actually published. Every CI claim (README "CI proves
   the build is self-contained", tracker rows 6/7, tracker closing line) is
   currently false in practice. **Fix:** push `main`, confirm the Actions run is
   green, then the rest of this review's verdict holds.

2. **SHOULD-FIX** — `README.md` opening: "never tunnels … never resolves a false
   overlap" contradicts the README's own `moveOneStep` description and the
   verified collinear quirk. Drop the absolutes.

3. **SHOULD-FIX** — `README.md:157` "No `while (true)`." contradicts
   `GameFlow.java:73`.

4. **SHOULD-FIX** — `README.md:127-133` implies `Ball.moveOneStep` has
   characterization tests; only its geometry dependencies do.

5. **SHOULD-FIX** — `claims-validation.md` row 1: "Verified by playthrough" is
   supported only by single-level evidence; the five-level progression and
   cross-level lives carry are unrun. Reword or do one full run.

6. **NIT** — `README.md:110` "current LTS" stale; "`geometry/` … fully
   unit-tested" overstated; `GameFlow.getLivesIndicator()` dead; `/simplify`
   should drop the redundant constructor `paddle.addToGame` (fixes the latent
   2× paddle-speed) and the duplicated frame-pacing block.

7. **NIT** — the documented acceptance script `mvn -B verify` works, but
   `mvn -o verify` on a machine with no `biuoop` in `~/.m2` fails with a
   file-repo-in-offline-mode error (§2.2). If offline builds are ever a goal,
   either pre-seed the artefact or document "first build needs network".

---

## 7. Disposition (PM, 2026-09-07)

| Finding | Action |
|---|---|
| §6.1 BLOCKER — work unpushed, CI never ran | **Open — needs the repo owner.** Cannot push without the owner's go-ahead. `claims-validation.md` now states this explicitly as the last acceptance item. |
| §6.2 SHOULD-FIX — README "never tunnels / never resolves a false overlap" | **Fixed** — opening paragraph reworded to "tested against every collidable along the line it is about to travel … resolves against the first block in its path", no absolutes. |
| §6.3 SHOULD-FIX — README "No `while (true)`" contradicts `GameFlow.java:73` | **Fixed** — "Real end state" bullet reworded; the loop is described as always-terminating (`break` / `return`), not absent. |
| §6.4 SHOULD-FIX — README implies `Ball.moveOneStep` has characterization tests | **Fixed** — now: tests pin "the primitives it is built from (`Point`, `Line` intersection math, `Rectangle`, `Velocity`)"; `moveOneStep` itself explicitly has no direct test. |
| §6.5 SHOULD-FIX — claims row 1 "Verified by playthrough" only single-level | **Fixed** — row 1 reworded: each level run individually, single-level win + 3-life loss confirmed, full 1→5 run not separately evidenced. |
| §5 NIT — README "Java 17 (current LTS)" stale | **Fixed** → "Java 17 (LTS)". |
| §5 NIT — README "geometry … fully unit-tested" | **Fixed** → "covered by characterization tests". |
| §4 NIT — `GameFlow.getLivesIndicator()` dead (new in Step 5) | **Fixed** — removed; `runLevels` uses the field directly, no other caller. |
| §3.6 NIT — paddle registered twice → 2× speed | **Not fixed (pre-existing, out of scope).** Recorded in `docs/lessons.md` "Known latent issues" with the re-tuning caveat. |
| §3.7 NIT — HUD sprites re-added each lost turn | **Not fixed (pre-existing).** Recorded in `docs/lessons.md`. |
| §4 NIT — double frame-pacing | **Not fixed (pre-existing).** Recorded in `docs/lessons.md`. |
| §4 NIT — `GameLevel.getBlockRemover/getBallRemover` dead | **Not fixed (pre-existing, deferred per brief).** |
| §6.7 NIT — `mvn -o verify` fails cold | **Documented** — `claims-validation.md` acceptance script uses `mvn -B verify` (no `-o`); cold online clone build verified (§2.2). |
