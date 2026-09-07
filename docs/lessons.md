# Lessons

Incidents worth not re-solving. Read before a debugging session.

---

## 2026-09-07 — The game had no losable end state

**What broke.** `GameFlow.runLevels` was a `while (true)` loop keyed on a `num`
sentinel derived from the block count. On losing every ball it showed
`GameOverScreen` for one key-press, then called `initializeBallAndPaddle()` and
looped — forever. There was a Game Over screen but it was never a terminal
state, and there were no lives.

**Root cause.** The turn outcome (cleared vs lost) was inferred inside the loop
from mutable counters instead of being returned as a value. `GameLevel.run()`
was `void`, so `runLevels` had nothing to branch on and fell back to "re-init
and continue".

**The fix (Step 5).**
- `GameLevel.run()` returns a `TurnResult` enum (`LEVEL_CLEARED` | `TURN_LOST`),
  computed from a `private boolean cleared` flag: reset at the top of `run()`,
  set in the blocks-cleared branch of `doOneFrame`.
- `GameFlow` owns a shared lives `Counter` (start 3), threaded by reference into
  every `GameLevel` exactly like the existing score indicator.
- `runLevels` replaces the unbounded loop + `num` sentinel with a turn-retry
  loop that always exits: `LEVEL_CLEARED` → `break` to the next level;
  `TURN_LOST` → `lives.decrease(1)`, and at `<= 0` → `GameOverScreen` then
  `return`, so `main` reaches `gui.close()` and the process ends. `WInScreen`
  only after the last level. (The loop is still written `while (true)` with
  explicit `break`/`return` — the point is that every path out is now reachable.)

**Gotcha.** There are two separate "ball" counters. `ballRemover`'s
`remainingBalls` is **per-turn** — `createBalls()` re-increments it every serve,
so it self-heals. The lives `Counter` is **per-run** and must never be reset
between turns. Conflating them either makes the game unlosable again or ends it
after one lost ball.

**Confirmed.** Playthrough 2026-09-07: lost 3 turns → "Game Over: Your score is
90" → space → window closes, process exits. No restart. "Lives: N" decrements
in the HUD across turns and levels.

**No automated coverage.** `GameFlow` / `GameLevel` need a live GUI, so the
loss→Game Over path is verified by hand-trace + `mvn verify` + manual
playthrough only. If that loop is touched again, re-run the 3-loss playthrough.

---

## Known latent issues (pre-existing, deliberately not fixed)

- **The paddle is registered twice.** `GameLevel` calls `paddle.addToGame(this)`
  in the constructor *and* in `initialize()`, with no dedup — so `Paddle`
  advances `2 × paddleSpeed()` per frame and is drawn twice. Present since the
  original assignment; every level's `paddleSpeed()` was tuned with the doubling
  in effect. Do **not** remove one call in isolation — it halves paddle speed on
  all five levels. Fix means dropping the constructor call *and* re-tuning every
  `paddleSpeed()`.
- **`initializeBallAndPaddle()` re-adds the score / level HUD sprites each lost
  turn.** Bounded (≤ 3 duplicates before Game Over), draws identical text at the
  same spot, no state impact. `livesIndicator` correctly avoids this — added
  once in `initialize()`.
- **Double frame-pacing.** Both `GameLevel.doOneFrame` and `AnimationRunner.run`
  sleep to a ~16 ms budget around the same call; the outer sleep is effectively
  dead.
