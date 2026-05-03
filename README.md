<!--suppress ALL -->
<p align="center">
    <img src="images/banner.png" alt="HuskSync" />
    <a href="https://github.com/MrPippi/HuskSync-26.1/actions/workflows/ci.yml">
        <img src="https://img.shields.io/github/actions/workflow/status/MrPippi/HuskSync-26.1/ci.yml?branch=master&logo=github"/>
    </a>
    <a href="https://github.com/MrPippi/HuskSync-26.1/releases">
        <img src="https://img.shields.io/github/v/release/MrPippi/HuskSync-26.1?color=00fb9a&label=Release" />
    </a>
    <a href="https://github.com/WiIIiam278/HuskSync">
        <img src="https://img.shields.io/badge/upstream-WiIIiam278%2FHuskSync-blue?logo=github" />
    </a>
    <br/>
    <b>
        <a href="https://github.com/MrPippi/HuskSync-26.1/releases">Releases</a>
    </b> —
    <b>
        <a href="https://william278.net/docs/husksync/setup">Setup (Upstream Docs)</a>
    </b> —
    <b>
        <a href="https://github.com/MrPippi/HuskSync-26.1/issues">Issues</a>
    </b>
</p>
<br/>

> [!WARNING]
> **This is an unofficial fork of [HuskSync](https://github.com/WiIIiam278/HuskSync) by [William278](https://william278.net/).** It is not affiliated with or endorsed by the original author. Use at your own risk. **Always back up your player data before installing or upgrading.**

---

**HuskSync** is a modern, cross-server player data synchronization system that enables the comprehensive synchronization of your user's data across multiple proxied servers. It does this by making use of Redis and a MySQL/Mongo/PostgreSQL to optimally cache data while players change servers.

## About This Fork

This fork adds support for **Paper 26.1.x** (Minecraft's new versioning scheme), which is not yet supported by the official release. All existing 1.21.x support is preserved.

**Changes from upstream (v3.8.8):**
- ✅ Added Paper 26.1.2 support
- 🔧 Updated NBT-API `2.15.5` → `2.15.7` (fixes startup failure on Paper 26.1.x version strings)
- 🔧 Updated Triumph-GUI `3.1.12` → `3.1.13` (improved 1.21.7+ inventory support)
- 🏗️ Added Gradle Java Toolchain support for auto-provisioning JDK 25

## Features
**⭐ Seamless synchronization** &mdash; Utilises optimised Redis caching when players change server to sync player data super quickly for a seamless experience.

**⭐ Complete player synchronization** &mdash; Sync inventories, Ender Chests, health, hunger, effects, advancements, statistics, locked maps & [more](https://william278.net/docs/husksync/sync-features)—no data left behind!

**⭐ Backup, restore & rotate** &mdash; Something gone wrong? Restore players back to a previous data state. Rotate and manage data snapshots in-game!

**⭐ Import existing data** &mdash; Import your MySQLPlayerDataBridge data—or from your existing world data! No server reset needed!

**⭐ Works great with Plan** &mdash; Stay in touch with your community through HuskSync analytics on your Plan web panel.

## Compatibility

You must download the correct JAR for your server's Minecraft version. Each JAR only loads on its target version.

|    Minecraft    | Java Version | Platforms | Support Status                 |
|:---------------:|:------------:|:----------|:-------------------------------|
|   **26.1.2**    |    **25**    | Paper     | ✅ **This fork**                |
|     1.21.11     |      21      | Paper     | ✅ Upstream active release      |
|     1.21.10     |      21      | Paper     | ✅ Upstream active release      |
|    1.21.7/8     |      21      | Paper, Fabric | ✅ Upstream active release  |
|     1.21.5      |      21      | Paper, Fabric | ✅ Upstream active release  |
|     1.21.4      |      21      | Paper, Fabric | ✅ Upstream active release  |
|     1.21.1      |      21      | Paper, Fabric | ✅ Upstream active release  |

> **Paper 26.1.2 requires Java 25.** Ensure your server JVM is Java 25 or newer.

## Setup
Requires a [MySQL/MariaDB/Mongo/PostgreSQL database](https://william278.net/docs/husksync/database), a [Redis (v5.0+) server](https://william278.net/docs/husksync/redis) and a network of compatible Paper servers.

1. **Back up your existing player data** before installing or upgrading.
2. Download the correct JAR for your server version from [Releases](https://github.com/MrPippi/HuskSync-26.1/releases).
3. Place the JAR in the `/plugins` directory of each server.
4. Start, then stop every server to let HuskSync generate config files.
5. Fill in your database and Redis credentials in `config.yml` on each server.
6. Start every server again — synchronization will begin.

## Development
Building requires **Java 25** (for the 26.1.2 module; Gradle will auto-provision it via [Foojay](https://foojay.io/) if not installed). Builds are output to `/target`:

```bash
./gradlew clean build
```

HuskSync uses `essential-multi-version` (Fabric) and `preprocessor` (Bukkit) to target multiple Minecraft versions from a single codebase.

### License
This fork is licensed under the Apache 2.0 license, the same as the upstream project.

- [License](LICENSE)
- [Upstream Repository](https://github.com/WiIIiam278/HuskSync)

### Support
> ⚠️ **This fork is community-maintained and unsupported by William278.** For issues specific to Paper 26.1.x support in this fork, please [open an issue](https://github.com/MrPippi/HuskSync-26.1/issues) here.
>
> For general HuskSync support, please refer to the [official project](https://github.com/WiIIiam278/HuskSync) (purchase required for Discord support).

## Links
- [Upstream HuskSync](https://github.com/WiIIiam278/HuskSync) &mdash; Original project by William278
- [Docs](https://william278.net/docs/husksync/) &mdash; Official plugin documentation
- [Issues (this fork)](https://github.com/MrPippi/HuskSync-26.1/issues) &mdash; Report fork-specific bugs

---
Fork maintained by [MrPippi](https://github.com/MrPippi). Based on [HuskSync](https://github.com/WiIIiam278/HuskSync) &copy; [William278](https://william278.net/), 2025. Licensed under the Apache-2.0 License.
