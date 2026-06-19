# 上游同步流程（Upstream Sync）

本 fork 與上游 [WiIIiam278/HuskSync](https://github.com/WiIIiam278/HuskSync) 的專案結構已**刻意分岔**：

- 本 fork 保留全部 Bukkit `1.21.x` + Fabric 目標，並自帶 Paper **26.1.2** 模組。
- 上游已移除 `1.21.x` 支援（PR #674），版本號走 `3.9.0`，並換用 neoforged licenser、gradle 9.5.1 等。

因此**不可**直接 `git merge upstream/master` —— 會把上游的結構決策（刪版本、改 build、版本號）一併拉進來，造成大量衝突並破壞本 fork 結構。

正確做法：**把上游當零件倉庫，只 cherry-pick 需要的程式修復，跳過 infra。**

---

## 判斷準則：撈什麼、跳什麼

| 類型 | 範例檔案 | 動作 |
| --- | --- | --- |
| **程式邏輯修復** | `common/src/.../*.java`、`bukkit/src/.../*.java`、`fabric/src/.../*.java` | ✅ cherry-pick |
| 建置設定 | `build.gradle`、`gradle.properties`、`settings.gradle` | ❌ 跳過 |
| CI / 發布 | `.github/workflows/*.yml` | ❌ 跳過 |
| 版本 bump | 只改版本號的 commit | ❌ 跳過 |
| 增刪版本支援 | 刪 `1.21.x`、改模組結構 | ❌ 跳過（與本 fork 衝突） |

> 重點：只有**改到實際執行行為**的 `.java` 修復才撈。infra 一律手動評估，預設跳過。

---

## 操作步驟

### 1. 抓上游最新狀態（只下載，不改程式）

```bash
git fetch upstream
```

### 2. 列出上游有、本 fork 沒有的 commit

```bash
git log --oneline master..upstream/master
```

### 3. 檢視每個 commit 改了哪些檔案

```bash
git show --stat <hash>
```

依上方準則篩出含 `src/.../*.java` 的真 bug 修復。

### 4. 在獨立分支上套用（不直接動 master）

```bash
git checkout -b sync-upstream-fix
git cherry-pick <hash>                 # 單一
# git cherry-pick <hash1> <hash2>      # 多個分散
# git cherry-pick <hashA>^..<hashB>    # 多個連續
```

### 5. 解衝突（若有）

```bash
# 編輯衝突檔案後：
git add <已解決檔案>
git cherry-pick --continue
# 或放棄：
git cherry-pick --abort
```

### 6. 建置驗證

```bash
./gradlew :bukkit:26.1.2:compileJava
./gradlew :common:test
```

> Windows：用 Git Bash 跑 `./gradlew`，或改 `gradlew.bat`。

### 7. 併回 master 並推送

```bash
git checkout master
git merge sync-upstream-fix
git push origin master
```

> 注意：推 `master` 會觸發 `ci.yml` 發布 alpha artifact。

---

## 重要觀念

- 全程**手動**，無自動排程、無背景觸發。
- `cherry-pick` 只套到**當前所在分支**，且只動**本地**——`git push` 後才上 GitHub。
- 在分支上操作 → master 永遠乾淨，出錯隨時 `git branch -D sync-upstream-fix` 丟掉。

---

## 現況快照（2026-06-19）

上游 `master` 領先本 fork 的 commit **全為 infra**（Paper 26.1.2 build、刪 1.21.x、Java 25 設定、gradle 9.5.1、版本 bump、CI 精簡），**無任何程式 bug 修復**。其中 `DataVersionSupplier` 的 `VERSION26_1 = 4786` 與本 fork 完全一致。

→ **目前無需 cherry-pick 任何 commit。** 本流程供未來上游出現 `.java` 修復時使用。
