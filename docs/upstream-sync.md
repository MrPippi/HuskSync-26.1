# Upstream Sync Workflow

This fork's project structure has **intentionally diverged** from upstream
[WiIIiam278/HuskSync](https://github.com/WiIIiam278/HuskSync):

- This fork keeps all Bukkit `1.21.x` + Fabric targets and ships its own Paper **26.1.2** module.
- Upstream has **removed** `1.21.x` support (PR #674), moved to version `3.9.0`, switched to the
  neoforged licenser, bumped to gradle 9.5.1, etc.

Because of this, **do not** run `git merge upstream/master` directly — it would pull in upstream's
structural decisions (removed versions, build changes, version bumps) wholesale, causing large
conflicts and breaking this fork's structure.

The correct approach: **treat upstream as a parts bin — cherry-pick only the code fixes you need,
and skip infra.**

---

## Decision Criteria: What to Pull vs Skip

| Type | Example files | Action |
| --- | --- | --- |
| **Code/logic fix** | `common/src/.../*.java`, `bukkit/src/.../*.java`, `fabric/src/.../*.java` | ✅ cherry-pick |
| Build config | `build.gradle`, `gradle.properties`, `settings.gradle` | ❌ skip |
| CI / release | `.github/workflows/*.yml` | ❌ skip |
| Version bump | commits that only change the version number | ❌ skip |
| Add/remove version support | removing `1.21.x`, restructuring modules | ❌ skip (conflicts with this fork) |

> Key point: only pull `.java` fixes that **change actual runtime behavior**. Infra is evaluated
> manually and skipped by default.

---

## Steps

### 1. Fetch the latest upstream state (download only, no code changes)

```bash
git fetch upstream
```

### 2. List commits upstream has that this fork doesn't

```bash
git log --oneline master..upstream/master
```

### 3. Inspect which files each commit changes

```bash
git show --stat <hash>
```

Use the criteria above to pick out the genuine bug fixes touching `src/.../*.java`.

### 4. Apply on an isolated branch (don't touch master directly)

```bash
git checkout -b sync-upstream-fix
git cherry-pick <hash>                 # single
# git cherry-pick <hash1> <hash2>      # several, scattered
# git cherry-pick <hashA>^..<hashB>    # several, contiguous range
```

### 5. Resolve conflicts (if any)

```bash
# After editing the conflicted files:
git add <resolved-files>
git cherry-pick --continue
# Or abort:
git cherry-pick --abort
```

### 6. Build and verify

```bash
./gradlew :bukkit:26.1.2:compileJava
./gradlew :common:test
```

> Windows: run `./gradlew` under Git Bash, or use `gradlew.bat`.

### 7. Merge back to master and push

```bash
git checkout master
git merge sync-upstream-fix
git push origin master
```

> Note: pushing to `master` triggers `ci.yml`, which publishes an alpha artifact.

---

## Key Concepts

- Everything is **manual** — no scheduled jobs, no background triggers.
- `cherry-pick` applies only to the **branch you're currently on**, and only **locally** — it
  reaches GitHub only after `git push`.
- Working on a branch keeps master clean — if something goes wrong, just drop it with
  `git branch -D sync-upstream-fix`.

---

## Current Snapshot (2026-06-19)

All commits upstream `master` is ahead by are **infra-only** (Paper 26.1.2 build, 1.21.x removal,
Java 25 config, gradle 9.5.1, version bump, CI cleanup) — **no code bug fixes**. The
`DataVersionSupplier` value `VERSION26_1 = 4786` is identical to this fork's.

→ **No commits need cherry-picking right now.** This workflow exists for the future, when upstream
ships a `.java` fix worth porting.
