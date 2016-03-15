# Assault And Battery Cycle
A Minecraft Bukkit plugin for the [Reddit UHC](https://www.reddit.com/r/ultrahardcore/) scenario
[Assault and Battery Cycle](https://redd.it/4aicpc)
made by [/u/Sean081799](https://www.reddit.com/user/Sean081799).

When PvP is enabled, the game switches between 3 cycles every 2 minutes:

- **Melee:** During the Melee cycle, direct player damage can only be dealt via melee attacks.
- **Ranged:** During the Ranged cycle, direct player damage can only be dealt via ranged attacks.
- **iPvP::** During the iPvP cycle, players cannot damage each other directly at all.

### Usage

**This plugin uses ghowden/Eluinhost's flagcommands.**
Click [here](https://github.com/Eluinhost/UHC/blob/master/docs/commands/Commands.md) for more information.

`/aabc set -e` - Enable the scenario (`-es` to enable silently)
`/aabc set -d` - Disable the scenario (`-ds` to disable silently)
`/aabc delay -d [Time]` - Change the cycle switch delay, supports timespans such as 12m30s, 45s, etc. (`-sd` to change silently)

---

This plugin is also available as a [compact script](http://pastebin.com/raw/CtrpgeLN) for the
[Skript](http://dev.bukkit.org/bukkit-plugins/skript/) plugin.
However, **the script version is not supported** and will likely not receive any bugfixes.
