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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TerraPluginConfig {
	// Locale Support
	String locale;
	
	// If Redstone Physics enabled?
	public boolean redStoneEnabled;
	
	// Clear Login/Logout message for certain Players
	public List<String> hideLoginMessage;
	
	// Vanished Players
	public List<String>	vanishedPlayers;

	// Nerf Invisibility Potions, will decrease time when taking damage.
	public boolean invisibilityPotions;
	public int invisibilityPotionsDamagePenalty;
	
	// Restrict Entity travels thru portal, stub bugfix for popular DUPE glitches.
	public boolean entityPortalTravel;
	public List<String> entityPortalTravelWorlds;

	// Splosion related: boosts explioson radius, increases damage, disables damage to blocks etc.
	public boolean explosions;
	public boolean explosionsNoDamageBlocks;
	public float explosionsRadiusMult;
	public List<String> explosionsRadiusMultEntities;
	
	// Make Dispensers shoot on higher distance.
	public boolean boostDispenser;
	public float boostDispenserMult;

	// Set Mobs Health on Spawn.
	public boolean customMobs;
	public Map<String, Integer> customMobsHealth;
	
	// Restrict PEARL and CHORUS teleport in some worlds
	public boolean itemTeleport;
	public boolean itemTeleportSpawnEnders;
	public List<String> itemTeleportReasons;
	public List<String> itemTeleportDisableInWorlds;
	
	// Restrict PISTONs to move some BLOCKs
	public boolean pistonRestrict;
	public List<String> pistonRestrictBlocks;
	
	// Set player Teleportation locations, from ender and nether teleports, stub fix for blocked portals.
	public boolean portalTarget;
	public Map<String,String> portalTargetLocations;
	
	// Random Ore Drops Module
	public boolean randomOreDrops;
	public List<String> randomOreDropsTools;
	public double randomOreDropsPerChunkLimit;
	public List<TerraPluginRandomOre> randomOreDropsOres;
	public Map<String,Float> randomOreDropsMultipliers;
	
	// World Height Limits
	public boolean restrictHeight;
	public Map<String, Integer>	restrictHeightLimits;
	
	// Battle Leavers
	public boolean damageTimer;
	public int damageTimerInterval;
	public List<String> damageTimerCause;
	public transient Map<String, Long> damageTimerLastRecieved;

	// Bed Respawn Protection
	public boolean bedSpawnProtection;
	public int bedSpawnProtectionRange;
	public transient Set<String> bedSpawnProtectionDead;
			
	// Nerf Mob Spawners
	public boolean mobSpawners;
	public int mobSpawnersExpMult;
	public double mobSpawnersDropChance;
	public List<String> mobSpawnersWorlds;
	public List<String> mobSpawnersEntities;

	// Nerf Elytras
	public boolean elytra;
	public List<String> elytraDisabledWorlds;

	// Drop Items After Death
	public boolean dropItems;
	public double dropItemsLevelPercent;
	public List<String> dropItemsList;	
	
	// TimeZone and Time Format
	public String timeZone;
	public String timeFormat;
	public String dateFormat;
	
	// Motd
	public boolean motd;
	public String[] motdFormat;
	
	// FullMoon Event Module
	public boolean fullMoon;
	public double fullMoonMonsterSpawnMult;
	public double fullMoonDropMult;
	public double fullMoonExpDropMult;

	// PlayerMods Module: Display Player mods and client in /ta info
	public boolean playerMods;
	public boolean playerModsWhiteList;
	public boolean playerModsWhiteListClients;
	public List<String> playerModsPluginChannels;
	public List<String> playerModsDisallowMods;
	public List<String> playerModsDisallowClients;
	
	// Chat Module: Local and Global Chat. 
	public boolean chat;
	public int chatLocalRange;
	public String chatJoinedServer;
	public String chatLeaveServer;
	public String chatMessage;
	
	// Advanced Arrows: Some Arrows Enhancement
	public boolean arrows;
	public boolean arrowsSpectralMoveDrops;
	public boolean arrowsCanPickupTipped;
	
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
			setDefaults();
			saveJsonConfig();
		}
		initTemporaryFields();
    }
    
	
	// Init fields that WILL NOT BE stored in JSON Config.
    public void initTemporaryFields(){
    	bedSpawnProtectionDead = new HashSet<String>();
    	damageTimerLastRecieved = new HashMap<String, Long>();
    }
    
    // Init fields that WILL BE stored in JSON Config.
    public void setDefaults(){
    	locale = "ru_RU";
    	redStoneEnabled = true;
    	
    	hideLoginMessage = new ArrayList<String>();
    	vanishedPlayers = new ArrayList<String>();
    	
    	invisibilityPotions = false;
    	invisibilityPotionsDamagePenalty = 30;
    	
    	entityPortalTravel = false;
    	entityPortalTravelWorlds = Arrays.asList("world","world_nether", "world_the_end");
    	
    	explosions = false;
    	explosionsNoDamageBlocks = true;
    	explosionsRadiusMult = 1;
    	explosionsRadiusMultEntities = Arrays.asList("WITHER","WITHER_SKULL","CREEPER","PRIMED_TNT","MINECART_TNT");
    	
    	boostDispenser = false;
    	boostDispenserMult = 2;
    	    	
    	customMobs = false;
    	customMobsHealth = new HashMap<String, Integer>();
    	customMobsHealth.put("ENDER_DRAGON",2000);
    	
    	itemTeleport = false;
    	itemTeleportSpawnEnders = true;
    	itemTeleportReasons = Arrays.asList("ENDER_PEARL","CHORUS_FRUIT");
    	itemTeleportDisableInWorlds = Arrays.asList("world", "world_nether");
    	
    	randomOreDrops = false;
    	randomOreDropsTools = Arrays.asList("IRON_PICKAXE", "DIAMOND_PICKAXE", "GOLD_PICKAXE");
    	randomOreDropsPerChunkLimit = 100;
    	randomOreDropsMultipliers = new HashMap<String, Float>();
    	randomOreDropsMultipliers.put("luck_enchantment", 5.0F);
    	randomOreDropsMultipliers.put("luck_potion", 10.0F);
    	randomOreDropsMultipliers.put("light", -50.0F);
    	randomOreDropsMultipliers.put("gold_pickaxe", 30.0F);
    	randomOreDropsMultipliers.put("full_moon", 15.0F);
    	
    	randomOreDropsOres = new ArrayList<TerraPluginRandomOre>();
    	randomOreDropsOres.add(new TerraPluginRandomOre("DIAMOND", 		"Diamond",	0.35d, 	5,	15,	0,	20, new HashSet<String>(Arrays.asList("ANY"))));
    	randomOreDropsOres.add(new TerraPluginRandomOre("GOLD_INGOT", 	"Gold",		0.85d,	3, 	5,	15,	30, new HashSet<String>(Arrays.asList("ANY"))));
    	randomOreDropsOres.add(new TerraPluginRandomOre("EMERALD", 		"Emerald", 	1.55d,	3, 	5,	25,	45, new HashSet<String>(Arrays.asList("ANY"))));

    	pistonRestrict = false;
    	pistonRestrictBlocks = Arrays.asList("SLIME_BLOCK");
    	
    	portalTarget = false;
    	portalTargetLocations = new HashMap<String, String>();
    	
    	restrictHeight = false;
    	restrictHeightLimits = new HashMap<String, Integer>();
    	restrictHeightLimits.put("world", 256);
    	restrictHeightLimits.put("world_nether", 128);
    	restrictHeightLimits.put("world_the_end", 256);
    	
    	damageTimer = false;
    	damageTimerInterval = 5;
    	damageTimerCause = Arrays.asList("ENTITY_ATTACK","PROJECTILE");
    	
    	bedSpawnProtection = false;
    	bedSpawnProtectionRange = 8;
    	
    	mobSpawners = false;
    	mobSpawnersExpMult = 0;
    	mobSpawnersDropChance = 30;
    	mobSpawnersWorlds = Arrays.asList("world","world_nether", "world_the_end");
    	mobSpawnersEntities = Arrays.asList("SKELETON","ZOMBIE","SPIDER","CAVE_SPIDER","CREEPER");
    	
    	playerMods = false;
    	playerModsWhiteList = false;
    	playerModsWhiteListClients = false;
    	playerModsPluginChannels = Arrays.asList("minecraft:brand","fml:handshake");
    	playerModsDisallowMods = Arrays.asList("x.?ray.*");
    	playerModsDisallowClients = Arrays.asList("example");

    	elytra = false;
    	elytraDisabledWorlds = Arrays.asList("world","world_nether","world_the_end");

    	dropItems=false;
    	dropItemsList=Arrays.asList("DIAMOND", "COAL_ORE", "COAL", "GOLD_ORE","GOLD_INGOT","LAPIS_ORE","DIAMOND_ORE","REDSTONE_ORE","EMERALD_ORE","IRON_INGOT","IRON_ORE","GOLDEN_APPLE","FLINT","ENDER_PEARL","BLAZE_ROD","EMERALD","ENDER_EYE");
    	dropItemsLevelPercent = 0.50d;
    	
		timeZone="Europe/Moscow";
    	timeFormat="HH:mm";
    	dateFormat="dd.MM.yy";
    	
    	fullMoon = false;
    	fullMoonMonsterSpawnMult = 2;
    	fullMoonDropMult = 2;
    	fullMoonExpDropMult = 2;
    	
    	chat = false;
    	chatLocalRange = 128;
    	chatJoinedServer="[&7%time%&f] *Player &2%plrname%&f joined the game.";
    	chatLeaveServer="[&7%time%&f] *Player &2%plrname%&f leaving the game.";
    	chatMessage="[&7%time%&f]%chatmode%<%prefix%%plrname%%suffix%&f> %msg%";
    	
		motd = true;
    	motdFormat = new String[] {"&3* Welcome, &f%plrname%! *", "&3- You played: &f%plrtime% &3hours!","&3- The date is &f%date% &3and time &f%time%", "&e* This is default motd! You can edit this in config.json *"};

    	arrows = false;
    	arrowsSpectralMoveDrops = true;
    	arrowsCanPickupTipped = false;
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
		String json = gson.create().toJson(this);
		try {
			Files.write(configFile, json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
