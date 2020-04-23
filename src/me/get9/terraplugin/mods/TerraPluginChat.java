package me.get9.terraplugin.mods;

import java.util.HashMap;
import java.util.Map;

import me.get9.terraplugin.TerraPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TerraPluginChat {
	
	// Players, that toggled local chat.
	private Map<String, Boolean> local;
	private TerraPlugin plugin;
	
	// Init
	public TerraPluginChat(TerraPlugin plugin){
		this.plugin = plugin;
		plugin.log("Chat module loaded!");
		local = new HashMap<String, Boolean>();
	}
	
	// Get Distance Between Two Players
	private static double getDistanceBetween(Player caster, Player target){
		if(!caster.getWorld().getName().equalsIgnoreCase(target.getWorld().getName())) return 9999;
		return caster.getLocation().distance(target.getLocation());
	}
	
	// Is Player In Local Chat?
	public boolean isLocal(String playerName){
		if(local.containsKey(playerName)){
			return local.get(playerName);
		}
		return false;
	}
	
	// Set Player Chat To Local.
	public void setLocal(String playerName, boolean state){
		local.put(playerName, state);
	}
	
	// Format Chat Type
	public String getChatMode(String name){
		if(isLocal(name)) return "§f[§eL§f]";
		return "§f[§aG§f]";
	}
	
	// Send Raw Message To All
	public void broadcastMessageToAll(String msg){
		for(Player target : Bukkit.getOnlinePlayers()){
			target.sendMessage(msg);
		}
	}
	
	// Send Formatted Chat Message.
	public void sendChatMessage(String msg, Player sender){
		String msgFormatted	= plugin.getUtils().formatChatMessage(msg, sender);
		for(Player target : Bukkit.getOnlinePlayers()){
			if(isLocal(target.getName().toLowerCase()) || isLocal(sender.getName().toLowerCase())){
				if(getDistanceBetween(sender, target) > plugin.getConf().chatLocalRange){
					continue;
				}
			}
			target.sendMessage(msgFormatted.replace("%chatmode%", getChatMode(target.getName().toLowerCase())));
		}
		plugin.log("[CHAT] "+sender.getName()+": "+msg);
	}
	

}
