package me.get9.terraplugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.get9.terraplugin.mods.randomoredrops.TerraPluginRandomOre;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TerraPluginConfig {
	// Locale Support
	String locale = "ru_RU";
	
	// If Redstone Physics enabled?
	public boolean redStoneEnabled = true;
	
	// Clear Login/Logout message for certain Players
	public List<String> hideLoginMessage = new ArrayList<String>();
	
	// Vanished Players
	public List<String>	vanishedPlayers = new ArrayList<String>();

	// Nerf Invisibility Potions, will decrease time when taking damage.
	public boolean invisibilityPotions = false;
	public int invisibilityPotionsDamagePenalty = 30;
	
	// Restrict Entity travels thru portal, stub bugfix for popular DUPE glitches.
	public boolean entityPortalTravel = false;
	public List<String> entityPortalTravelWorlds = Arrays.asList("world","world_nether", "world_the_end");

	// Splosion related: boosts explioson radius, increases damage, disables damage to blocks etc.
	public boolean explosions = false;
	public boolean explosionsNoDamageBlocks = true;
	public float explosionsRadiusMult = 1.0F;
	public List<String> explosionsRadiusMultEntities = Arrays.asList("WITHER","WITHER_SKULL","CREEPER","PRIMED_TNT","MINECART_TNT");
	
	// Make Dispensers shoot on higher distance.
	public boolean boostDispenser = false;
	public float boostDispenserMult = 2.0F;

	// Set Mobs Health on Spawn.
	public boolean customMobs = false;
	public Map<String, Integer> customMobsHealth = ImmutableMap.of("ENDER_DRAGON", 2000);

	// Restrict PEARL and CHORUS teleport in some worlds
	public boolean itemTeleport = false;
	public boolean itemTeleportSpawnEnders = true;
	public List<String> itemTeleportReasons = Arrays.asList("ENDER_PEARL","CHORUS_FRUIT");
	public List<String> itemTeleportDisableInWorlds = Arrays.asList("world", "world_nether");
	
	// Restrict PISTONs to move some BLOCKs
	public boolean pistonRestrict = false;
	public List<String> pistonRestrictBlocks = Arrays.asList("SLIME_BLOCK");
	
	// Set player Teleportation locations, from ender and nether teleports, stub fix for blocked portals.
	public boolean portalTarget = false;
	public Map<String,String> portalTargetLocations = new HashMap<String, String>();

	// Random Ore Drops Module
	public boolean randomOreDrops = false;
	public List<String> randomOreDropsTools = Arrays.asList("IRON_PICKAXE", "DIAMOND_PICKAXE", "GOLD_PICKAXE");
	public double randomOreDropsPerChunkLimit = 100;
	public List<TerraPluginRandomOre> randomOreDropsOres = Arrays.asList(
				new TerraPluginRandomOre("DIAMOND", "Diamond", 0.35d, 5, 15, 0, 20, new HashSet<String>(Arrays.asList("ANY"))),
				new TerraPluginRandomOre("GOLD_INGOT", "Gold", 0.85d, 3, 5, 15, 30, new HashSet<String>(Arrays.asList("ANY"))),
				new TerraPluginRandomOre("EMERALD", "Emerald", 1.55d, 3, 5, 25, 45, new HashSet<String>(Arrays.asList("ANY")))
			);
	public Map<String,Float> randomOreDropsMultipliers = ImmutableMap.of(
				"luck_enchantment", 5.0F,
				"luck_potion", 10.0F,
				"light", -50.0F,
				"gold_pickaxe", 30.0F,
				"full_moon", 15.0F
			);
	
	// World Height Limits
	public boolean restrictHeight = false;
	public Map<String, Integer>	restrictHeightLimits = ImmutableMap.of(
				"world", 256,
				"world_nether", 128,
				"world_the_end", 256
			);
	
	// Battle Leavers
	public boolean damageTimer = false;
	public int damageTimerInterval = 5;
	public List<String> damageTimerCause = Arrays.asList("ENTITY_ATTACK","PROJECTILE");
	public transient Map<String, Long> damageTimerLastRecieved = new HashMap<String, Long>();
	
	// Bed Respawn Protection
	public boolean bedSpawnProtection = false;
	public int bedSpawnProtectionRange = 8;
	public transient Set<String> bedSpawnProtectionDead = new HashSet<String>();

	// Nerf Mob Spawners
	public boolean mobSpawners = false;
	public int mobSpawnersExpMult = 0;
	public double mobSpawnersDropChance = 30;
	public List<String> mobSpawnersWorlds = Arrays.asList("world","world_nether", "world_the_end");
	public List<String> mobSpawnersEntities = Arrays.asList("SKELETON","ZOMBIE","SPIDER","CAVE_SPIDER","CREEPER");
	
	// Nerf Elytras
	public boolean elytra = false;
	public List<String> elytraDisabledWorlds = Arrays.asList("world","world_nether","world_the_end");

	// Drop Items After Death
	public boolean dropItems = false;
	public double dropItemsLevelPercent = 0.50d;
	public List<String> dropItemsList = Arrays.asList("DIAMOND", "COAL_ORE", "COAL", "GOLD_ORE","GOLD_INGOT","LAPIS_ORE","DIAMOND_ORE","REDSTONE_ORE","EMERALD_ORE","IRON_INGOT","IRON_ORE","GOLDEN_APPLE","FLINT","ENDER_PEARL","BLAZE_ROD","EMERALD","ENDER_EYE");	
	
	// TimeZone and Time Format
	public String timeZone="Europe/Moscow";
	public String timeFormat="HH:mm";
	public String dateFormat="dd.MM.yy";
		
	// Motd
	public boolean motd = true;
	public String[] motdFormat = new String[] {"&3* Welcome, &f%plrname%! *", "&3- You played: &f%plrtime% &3hours!","&3- The date is &f%date% &3and time &f%time%", "&e* This is default motd! You can edit this in config.json *"};
	
	// FullMoon Event Module
	public boolean fullMoon = false;
	public double fullMoonMonsterSpawnMult = 2;
	public double fullMoonDropMult = 2;
	public double fullMoonExpDropMult = 2;
	
	// PlayerMods Module: Display Player mods and client in /ta info
	public boolean playerMods = false;
	public boolean playerModsWhiteList = false;
	public boolean playerModsWhiteListClients  = false;
	public List<String> playerModsPluginChannels = Arrays.asList("minecraft:brand", "fml:handshake");
	public List<String> playerModsDisallowMods = Arrays.asList("x.?ray.*");
	public List<String> playerModsDisallowClients = Arrays.asList("example");
	
	// Chat Module: Local and Global Chat. 
	public boolean chat = false;
	public boolean chatTeamsSupport = true;
	public int chatLocalRange = 128;
	public String chatJoinedServer="[&7%time%&f] *Player %teamPrefix%&2%plrname%&f joined the game.";
	public String chatLeaveServer="[&7%time%&f] *Player %teamPrefix%&2%plrname%&f leaving the game.";
	public String chatMessage="[&7%time%&f]%chatmode%%teamPrefix%&f<%prefix%%plrname%%suffix%&f> %msg%";	
	
	// Advanced Arrows: Some Arrows Enhancement
	public boolean arrows = false;;
	public boolean arrowsSpectralMoveDrops = true;
	public boolean arrowsCanPickupTipped = false;	
	
	// Internal Variables
	private transient Path configFile;
	
	public TerraPluginConfig(Path dataFolder, String fileName){
    	this.configFile = Paths.get(dataFolder + File.separator + fileName);
		if(!Files.exists(dataFolder)){
			try {
				Files.createDirectory(dataFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!Files.exists(configFile)){
			saveJsonConfig();
		}
    }
	
	public static TerraPluginConfig loadJsonConfig(Path dataFolder, String fileName){
		String json = "";
		TerraPluginConfig temp = new TerraPluginConfig(dataFolder, fileName);
		try {
			json = new String(Files.readAllBytes(temp.configFile));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			temp = new Gson().fromJson(json, TerraPluginConfig.class);
			temp.configFile = Paths.get(dataFolder + File.separator + fileName);
		}
		return temp;
	}
	
	public void saveJsonConfig(){
		GsonBuilder gson = new GsonBuilder();
		gson.setPrettyPrinting();
		gson.disableHtmlEscaping();
		gson.serializeNulls();
		String json = gson.create().toJson(this);
		try {
			Files.write(configFile, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
