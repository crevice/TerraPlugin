package me.get9.terraplugin;

import java.util.logging.Logger;

import me.get9.terraplugin.listeners.TerraPluginCommandExecutor;
import me.get9.terraplugin.listeners.TerraPluginListener;
import me.get9.terraplugin.listeners.TerraPluginMessageListener;
import me.get9.terraplugin.mods.TerraPluginChat;
import me.get9.terraplugin.mods.TerraPluginFullMoon;
import me.get9.terraplugin.mods.playermods.TerraPluginPlayerMods;
import me.get9.terraplugin.mods.randomoredrops.TerraPluginRandomOreDrops;
import me.get9.terraplugin.utils.TerraPluginUtils;

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
	private TerraPluginPlayerMods modsList;
	private TerraPluginFullMoon fullMoon;
	private TerraPluginChat chat;
	private TerraPluginUtils utils;
	private TerraPluginRandomOreDrops randomOreDrops;
	
	public Permission adminperm;

	private BukkitScheduler scheduler;
	
	public void onEnable(){
		// Init Main Vars
		plugin 		= this;
		log 		= Logger.getLogger("Minecraft");
		// Load Config
		reloadConfig();
		
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
	
	public void onDisable(){
		config.saveJsonConfig();
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
		config = TerraPluginConfig.loadJsonConfig(plugin.getDataFolder().toPath(), "config.json");
		config.initTemporaryFields();
	}
	
	public TerraPluginUtils getUtils(){
		return utils;
	}
	
	public void log(String msg){
		log.info("[" + plugin.getName() + "] " + msg);
	}
}