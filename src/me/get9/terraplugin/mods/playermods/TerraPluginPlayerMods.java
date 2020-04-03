package me.get9.terraplugin.mods.playermods;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.get9.terraplugin.TerraPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TerraPluginPlayerMods {
	public Map<String, TerraPluginPlayerModsRecord> playerMods;
	
	private TerraPlugin plugin;
	
	public TerraPluginPlayerMods (TerraPlugin plugin){
		this.plugin = plugin;
		plugin.log("Mods List module loaded!");
		playerMods = new HashMap<String, TerraPluginPlayerModsRecord>();
	}
	
	public void createRecord(String playerName, TerraPluginPlayerModsRecord record){
		playerMods.put(playerName, record);
	}
	
	public void addModToRecord(String playerName, String mod, String version){
		playerMods.get(playerName).addMod(mod, version);
	}
	
	public void clearRecord(String playerName){
		playerMods.remove(playerName);
	}
	
    public void checkDisabledMods(String plrName){
    	if(playerMods.containsKey(plrName)){
    		TerraPluginPlayerModsRecord record = playerMods.get(plrName);
    		Player plr = Bukkit.getServer().getPlayer(plrName);
    		if(plr == null) return;
    		// Check if clientbrand is banned
    		for(String b : plugin.getConf().playerModsDisallowClients){
        		if(regexString(record.getClientBrand(), b)){
        			plr.kickPlayer("Данная версия клиента не поддерживается на сервере.");
        			return;
        		}
    		}
    		// Check if mod is banned
			for(String r: record.getMods()){
	    		for(String m : plugin.getConf().playerModsDisallowMods){
	    			if(regexString(r, m)){
	        			plr.kickPlayer("Один из установленных модов не поддерживается на сервере.");
	        			return;
	    			}
	    		}
			}
    	}
    }

    private boolean regexString(String str, String pattern){  
        Pattern p = Pattern.compile(pattern);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    }  
    
    private void sendFmlPacket(Player player, byte... data)
    {
        player.sendPluginMessage(plugin, "FML|HS", data);
    }
    
	public void queueCheckDisabledMods(final String plrName){
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
            	checkDisabledMods(plrName);
            }
        }.runTaskLater(plugin, 100L);
	}
    // Broken in 1.15.2, Recommend to Turn Off PlayerMods
	public void requestFMLModsList(final Player player){
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                sendFmlPacket(player, (byte) -2, (byte) 0);
                sendFmlPacket(player, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
                sendFmlPacket(player, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
            }
        }.runTaskLater(plugin, 20L);
	}
}
