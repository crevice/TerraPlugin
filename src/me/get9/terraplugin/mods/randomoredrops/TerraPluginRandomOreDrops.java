package me.get9.terraplugin.mods.randomoredrops;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.get9.terraplugin.TerraPlugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class TerraPluginRandomOreDrops implements Runnable{
	private TerraPlugin plugin;
	private Map<String, Double> oreCount;
	public TerraPluginRandomOreDrops(TerraPlugin plugin){
		this.plugin = plugin;
		oreCount = new HashMap<String, Double>();
		plugin.log("Random Ore Drops module loaded!");
	}

	// Chunk location to String, for ez storing
	public String chunkLocToString(Location loc){
		return loc.getChunk().getX() + "," + loc.getChunk().getZ();
	}
	
	// Get Ore Count Already Mined In Chunk
	public double getOreMinedInChunk(String chunk){
		double mined = 0.0;
		if(oreCount.containsKey(chunk)){
			mined = oreCount.get(chunk);
		}
		return mined;
	}

	// Return Ore Left in chunk in percentage 0.0 / 1.0
	public double getOrePercent(String chunk){
		return (plugin.getConf().randomOreDropsPerChunkLimit - getOreMinedInChunk(chunk)) / plugin.getConf().randomOreDropsPerChunkLimit;
	}
	
	// Increase/Decrease Ore Count in chunk, Run in Scheduler, or manually
	public void tickOreCount(String chunk, double tickNum){
		double mined = getOreMinedInChunk(chunk);
		if(mined + tickNum <= 0){
			oreCount.put(chunk, 0d);
		}else if(mined + tickNum > plugin.getConf().randomOreDropsPerChunkLimit){
			oreCount.put(chunk, plugin.getConf().randomOreDropsPerChunkLimit);
		}else{
			oreCount.put(chunk, mined+tickNum);
		}
	}
	
	// Show Ore Information on Current Location
	public String showOreInfo(Location loc){
		String chunk = chunkLocToString(loc);
		// Colored Bar
		int index = (int) Math.ceil(getOrePercent(chunk)*10);
		String[] bar = "||||||||||".split("");
		String barColor = "§a";
		if(index == 0){
			barColor = "§7";
		}else if(index < 3){
			barColor = "§c";
		}else if(index < 6){
			barColor = "§6";
		}
		if(index > 0){
			bar[index-1]="|§7";
		}
		// Display Ores Left
		String info = "§e-= [ ÐÓÄÀ "+ barColor;
		info+=String.join("", bar)+" §e] [ §9";
		// Display Ores Available Here
		for(TerraPluginRandomOre o : plugin.getConf().randomOreDropsOres){
			if(loc.getY() >= o.minHeight && loc.getY() <= o.maxHeight){
				if(o.biome.contains("ANY") || o.biome.contains(loc.getBlock().getBiome().name())){
					info+=o.name + " ";
				}
			}
		}
		info+="§e] =-";
		return info;
	}
	
    public TerraPluginRandomOre getRandomOre(Player plr, Location loc){
		String chunk = chunkLocToString(loc);
		// If Limit Reached, Don't Drop Anything
    	if(getOreMinedInChunk(chunk) >= plugin.getConf().randomOreDropsPerChunkLimit){
    		return null;
    	}
    	
    	// If Mined By Pickaxe
		if(plr.getInventory().getItemInMainHand() != null && plugin.getConf().randomOreDropsTools.contains(plr.getInventory().getItemInMainHand().getType().toString())){
			double mult = 0;
			double random = plugin.getUtils().getRandomDouble(0, 100);
	    	
			// Lucky Miner Enchantment
			if(plugin.getConf().randomOreDropsMultipliers.containsKey("luck_enchantment")){
				if(plr.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)){
					mult+=plugin.getConf().randomOreDropsMultipliers.get("luck_enchantment") * plr.getInventory().getItemInMainHand().getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS);
				}
			}
			
			// Lucky Potion
			if(plugin.getConf().randomOreDropsMultipliers.containsKey("luck_potion")){
				if(plr.hasPotionEffect(PotionEffectType.LUCK)){
					mult+=(1+plr.getPotionEffect(PotionEffectType.LUCK).getAmplifier()) * plugin.getConf().randomOreDropsMultipliers.get("luck_potion");
				}
			}
				
			// Less Rate In Darkness
			if(plugin.getConf().randomOreDropsMultipliers.containsKey("light")){
				if(plr.getLocation().add(0, 1, 0).getBlock().getLightLevel() < 1 && !plr.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
					mult+=plugin.getConf().randomOreDropsMultipliers.get("light");
				}
			}
			
			// Golden Pickaxe
			if(plugin.getConf().randomOreDropsMultipliers.containsKey("gold_pickaxe")){
				if(plr.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_PICKAXE)){
					mult+=plugin.getConf().randomOreDropsMultipliers.get("gold_pickaxe");
				}
			}
				
			// More Rate in Full Moon
			if(plugin.getConf().randomOreDropsMultipliers.containsKey("full_moon")){
				if(plugin.getConf().fullMoon && plugin.getFullMoon().running){
					mult+=plugin.getConf().randomOreDropsMultipliers.get("full_moon");
				}
			}
			
			// Roll Dice
			for(TerraPluginRandomOre o : plugin.getConf().randomOreDropsOres){
				if(loc.getY() >= o.minHeight && loc.getY() <= o.maxHeight){
					if(o.biome.contains("ANY") || o.biome.contains(loc.getBlock().getBiome().name())){
						double chance = o.chance + o.chance*(mult/100);
						if(random < chance){
							plugin.log(plr.getName() + " got random ore: " + o.type + ", chance " + String.format("%.2f", random) + "/" + String.format("%.2f", chance));
							tickOreCount(chunk, o.weight);
							return o;
						}
					}
				}
			}
		}
		return null;
    }
    
	@Override
	public void run() {
		//ToDo: Random tick count
		Set<String> purge = new HashSet<String>();
		for(Map.Entry<String, Double> e : oreCount.entrySet()){
			if(e.getValue()-10 <= 0){
				purge.add(e.getKey());
			}
			tickOreCount(e.getKey(), plugin.getUtils().getRandomDouble(8,24));
			//plugin.log("Chunk " + e.getKey() + ":"+ e.getValue()+" / "+ oreCount.size());
		}
		// Clear Not Used Chunks
		oreCount.keySet().removeAll(purge);
	}
}
