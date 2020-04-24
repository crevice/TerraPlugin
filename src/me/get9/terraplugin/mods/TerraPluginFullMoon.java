package me.get9.terraplugin.mods;

import java.util.HashMap;
import java.util.Map;

import me.get9.terraplugin.TerraPlugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class TerraPluginFullMoon implements Runnable{
	public boolean running;
	private Map<String,Integer> defaultSpawnLimit;
	private TerraPlugin plugin;

	// Init
	public TerraPluginFullMoon(TerraPlugin plugin){
		this.plugin = plugin;
		plugin.log("Full Moon module loaded!");
		running = false;
		defaultSpawnLimit = new HashMap<String, Integer>();
		for (World w : Bukkit.getServer().getWorlds()){
			defaultSpawnLimit.put(w.getName(), w.getMonsterSpawnLimit());
		}
	}
	
	// Multiply Spawn Rate For All Worlds
	public void setSpawnLimitRate(double rate){
		for (World w : Bukkit.getServer().getWorlds()){
			w.setMonsterSpawnLimit((int)(defaultSpawnLimit.get(w.getName()) * rate));
		}
	}
	
	// Is Day? 
	// 1000=07:00 (/time set day) ; 13000=19:00 (/time set night)
	public boolean isDay(){
		long time = plugin.getServer().getWorlds().get(0).getTime();
	    if(time > 1000 || time < 13000) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	// Get MoonPhase Value
	// 0 - Full Moon (White), 4 = New Moon (Black)
	public int getMoonPhase(){
		return (int) ((plugin.getServer().getWorlds().get(0).getFullTime() / 24000L) % 8);
	}
	
	// Check MoonPhase
	public boolean isFullMoon(){
		if(getMoonPhase() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	// Aggro Mobs to Nearby Player
	public void aggroMobsToNearbyPlayers(double radius){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getGameMode().equals(GameMode.CREATIVE)){
				continue;
			}
			for(Entity e: p.getNearbyEntities(radius, radius, radius)){
				if(e instanceof Monster){
					Monster m = (Monster) e;
					if(m.getTarget() == null){
						m.setTarget(p);
					}
				}
			}
		}
	}
	
	// Run FullMoon Event
	@Override
	public void run() {
		// Don't run if no players, and night not yet started, check moon phase.
		if(Bukkit.getOnlinePlayers().size() > 0 && !running && !isDay() && isFullMoon()){
			running = true;
    		setSpawnLimitRate(plugin.getConf().fullMoonMonsterSpawnMult);
    		plugin.getUtils().showTitleToAll(plugin.getLocale("fullmoonTitle"), plugin.getLocale("fullmoonSubTitle"));
    		plugin.log("Full Moon Started!");
		} else if(running && isDay()) {
			running = false;
    		setSpawnLimitRate(1);
    		plugin.getUtils().showTitleToAll(plugin.getLocale("fullmoonEndTitle"), plugin.getLocale("fullmoonEndSubTitle"));
    		plugin.log("Full Moon Ended!");
		}
		
		// Do Some Things while FullMoon is Running
		if(running){
			aggroMobsToNearbyPlayers(30.0);
		}
	}
}
