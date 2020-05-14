package me.get9.terraplugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import me.get9.terraplugin.listeners.TerraPluginCommandExecutor;
import me.get9.terraplugin.listeners.TerraPluginListener;
import me.get9.terraplugin.listeners.TerraPluginMessageListener;
import me.get9.terraplugin.locale.TerraPluginLocale;
import me.get9.terraplugin.mods.TerraPluginChat;
import me.get9.terraplugin.mods.fullmoon.TerraPluginFullMoon;
import me.get9.terraplugin.mods.playermods.TerraPluginPlayerMods;
import me.get9.terraplugin.mods.randomoredrops.TerraPluginRandomOreDrops;
import me.get9.terraplugin.utils.TerraPluginUtils;
import me.get9.terraplugin.utils.TerraPluginVaultManager;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class TerraPlugin extends JavaPlugin {
	private TerraPlugin plugin;
	private Logger log;
	private TerraPluginListener listener;
	private TerraPluginMessageListener plglistener;
	private TerraPluginCommandExecutor executor;
	private TerraPluginConfig config;
	private Path configFile;
	private Path localeFile;
	private TerraPluginPlayerMods modsList;
	private TerraPluginFullMoon fullMoon;
	private TerraPluginChat chat;
	private TerraPluginUtils utils;
	private TerraPluginRandomOreDrops randomOreDrops;
	private TerraPluginLocale locale;
	private TerraPluginVaultManager vault;
	
	public Permission adminperm;
	private BukkitScheduler scheduler;
	
	
	// Whel Plugin Enabled
	public void onEnable(){
		// Init Main Vars
		plugin 		= this;
		log 		= Logger.getLogger("Minecraft");
		
		// Load Config
		configFile = Paths.get(plugin.getDataFolder().toString() + File.separator + "config.json");
		reloadConfig();
		
		// Init Locale
		localeFile = Paths.get(plugin.getDataFolder().toString() + File.separator + "locale_" + config.locale + ".json");
        locale = TerraPluginLocale.loadLocaleFile(localeFile);
        
		// Init Vault
		if(config.useVault){
			vault = new TerraPluginVaultManager(this);
		}
		
		// Init Main Classes
		adminperm 	= new Permission("terra.common");
		listener 	= new TerraPluginListener(plugin);
		executor 	= new TerraPluginCommandExecutor(plugin);
        utils 		= new TerraPluginUtils(plugin);
        scheduler 	= getServer().getScheduler();
        
        // Register Main Listeners
		getServer().getPluginManager().registerEvents(listener, this);
        getCommand("ta").setExecutor(executor);
        getCommand("bed").setExecutor(executor);

        // FullMoon Extension
        if(config.fullMoon){
            fullMoon = new TerraPluginFullMoon(plugin);
            scheduler.runTaskTimer(plugin, fullMoon, 400L, 400L);
        }
        
        // Chat Manager Extension
        if(config.chat){
        	chat = new TerraPluginChat(plugin);
            getCommand("chat").setExecutor(executor);
        }
        
        // Random Ore Drops Extension
        if(config.randomOreDrops){
        	randomOreDrops = new TerraPluginRandomOreDrops(plugin);
            scheduler.runTaskTimer(plugin, randomOreDrops, 12000L, 12000L);
        }
        
        // Motd Command
        if(config.motd){
            getCommand("motd").setExecutor(executor);
        }
        
        // Mods List Extension
        if(config.playerMods){
    		plglistener = new TerraPluginMessageListener(plugin);
    		modsList = new TerraPluginPlayerMods(plugin);
    		for(String channel : config.playerModsPluginChannels){
    			if(!getServer().getMessenger().isIncomingChannelRegistered(plugin, channel)){
        			getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, plglistener);
    			}
    			if(!getServer().getMessenger().isOutgoingChannelRegistered(plugin, channel)){
    				getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
    			}
    		}
        }
	}

	// When Plugin Disabled
	public void onDisable(){
		if(config.fullMoon){
			fullMoon.setSpawnLimitRate(1);
		}		
		saveConfig();
	}
	
    public TerraPluginVaultManager getVault() {
        return vault;
    }
    
	public String getLocale(String var){
		return locale.getString(var, null);
	}
	
	public String getLocale(String var, Object[] params){
		return locale.getString(var, params);
	}
	
	public BukkitScheduler getScheduler(){
		return scheduler;
	}
	
	public TerraPluginConfig getConf(){
		return config;
	}
	
	public TerraPluginRandomOreDrops getRandomOreDrops(){
		return randomOreDrops;
	}
	
	public TerraPluginFullMoon getFullMoon(){
		return fullMoon;
	}
	
	public TerraPluginPlayerMods getModsList(){
		return modsList;
	}
	
	public TerraPluginChat getChat(){
		return chat;
	}
	
	public void reloadConfig(){
		config = TerraPluginConfig.updateConfig(configFile);
	}
	
	public void saveConfig(){
		config.saveJsonConfig(configFile);
	}
	
	public TerraPluginUtils getUtils(){
		return utils;
	}
	
	public void log(String msg){
		log.info("[TerraPlugin] " + msg);
	}
}