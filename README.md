# TerraPlugin
A set of utilities and fixes for the server.
# Features:
### Admin-Tools:
- Vanish: Hide admin from players.
- NoAnnounce: Remove admin from Enter\Leave server announcements.
- Restrict Mods and Clients (Unreliable)

### Bug-fixes:
- Can Stop Redstone Activity for AntiLag reasons.
- Stop Entity travel thru portals (older dupe fix).
- Deny Pistons to move some of restricted blocks (older dupe fix).
- Set global portal location for worlds ex. on spawn (fixes blocked portals in regions).
- Restrict World Height. (Kill players who flying to high on elytra, or travel on top on nether)
- Add Handicap to players that leave server while in battle.
- BedSpawnProtection, move players that being sandwitched near bed to the safe place.
- Nerf Mob Spawners, with exp and drop rates.
- Disable Elytras in some worlds.

### Mechanics:
- Remove time from Invisibility Effect, when taking damage.
- Nerf or increase damage from explosions.
- Boost Dispenser shooting distance.
- Set custom mob health, ex boost enderdragon.
- Restrict Enderpearls and Chorus in some worlds.
- Configurable MOTD and CHAT format.
- Local/Global Chat
- Spectral Arrows can teleport mob drop to the player.
- Unpickable Tipped Arrows
- FullMoon Event: Change Spawn and Droprate during FullMoon.
- Random Ore Drops! Gambling is now in minecraft!


 ### Admin-Only Commands
| Command            | Arguments           | Action 
|--------------------|---------------------|--------------------------------------------------------------------------------------|
| /ta openinv        | player_name         | Open player inventory.
| /ta openend        | player_name         | Open player enderchest.
| /ta info           | player_name         | Show some info about player.
| /ta setspeed       | player_name         | Set player movement speed.
| /ta vanish         |                     | Hide player (Invisibility).
| /ta setannounce    |                     | Remove player Join/Exit announcements.
| /ta redpower       | world x y z         | (LEGACY) Spawns Redstone block on X Y Z, for short amount of time. 
| /ta redstop        |                     | Freeze Redstone activity (AntiLag).
| /ta setportal      |                     | Set Portal exit location for this world.
| /ta help           |                     | Show small help article for commands.
| /ta reload         |                     | Reload config file.
| /ta save           |                     | Show Small Help Article for commands.
| /ta help           |                     | Show Small Help Article for commands.

 ### Player Commands
| Command            | Arguments           | Action 
|--------------------|---------------------|--------------------------------------------------------------------------------------|
| /bed               |                     | Teleport player to bed location. (If Saved)
| /motd              |                     | Shows MOTD.
| /chat              |                     | Toggle chat status GLOBAL/LOCAL.

## Compiling And Running Requirements:
>- Spigot (1.15.2+)
>- Google/GSON Lib (2.6.2+)
>- Vault (https://github.com/milkbowl/Vault)
