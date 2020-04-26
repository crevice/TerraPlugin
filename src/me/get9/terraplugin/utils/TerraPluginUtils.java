package me.get9.terraplugin.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import me.get9.terraplugin.TerraPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class TerraPluginUtils {
	private TerraPlugin plugin;
	
	public TerraPluginUtils(TerraPlugin plugin){
		this.plugin = plugin;
	}
	
	// Translate Location to String.
	public String locToString(Location loc){
		return loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ();
	}
	
	// Translate String to Location
	public Location stringToLoc(String str){
		String[] strarr = str.split(",");
		World world = plugin.getServer().getWorld(strarr[0]);
		if(world == null || strarr.length != 4){
			return null;
		}
		double x = Double.parseDouble(strarr[1]);
		double y = Double.parseDouble(strarr[2]);
		double z = Double.parseDouble(strarr[3]);
		return new Location(world,x,y,z);
	}
		
	public String formatChatMessage(String msg, Player plr){
		if(plugin.getConf().chatAllowColors){
			msg = msg.replaceAll("&([rlonmka-f0-9])", "\u00A7$1");
		}
		return formatMessage(plugin.getConf().chatMessage, plr).replace("%msg%", msg);
	}
	
	public String formatMessage(String msg, Player plr){
		DateFormat dateFormat = new SimpleDateFormat(plugin.getConf().timeFormat + "<>" +plugin.getConf().dateFormat);
		dateFormat.setTimeZone(TimeZone.getTimeZone(plugin.getConf().timeZone));
		String dateTime = dateFormat.format(new Date());
		String time = dateTime.split("<>")[0];
		String date = dateTime.split("<>")[1];
		// Format Scoreboard Teams, by Minecraft 
		if(plugin.getConf().chatTeamsSupport){
			msg = formatTeam(msg, plr);
		}
		// Format Prefixes and Suffixes by Permissions System
		if(plugin.getConf().useVault){
			msg = formatPermissions(msg, plr);
		}
		msg = msg
				.replaceAll("%time%", time)
				.replaceAll("%date%", date)
				.replaceAll("%plrname%", plr.getName())
				.replaceAll("%plrtime%", plr.getTicksLived()/20/60/60+"")
				.replaceAll("(\\$|\\\\)", "\\\\$0")
				.replaceAll("&", "\u00A7");
		return msg;
	}
	
	public String formatPermissions(String msg, Player plr){
		msg = msg
				.replaceAll("%prefix%", plugin.getVault().chat.getPlayerPrefix(plr))
				.replaceAll("%suffix%", plugin.getVault().chat.getPlayerSuffix(plr));
		return msg;
	}
	
	public String formatTeam(String msg, Player plr){
		Team team = plr.getScoreboard().getEntryTeam(plr.getName());
		if(team == null || plugin.getConf().chatTeamsSupport == false){
			return msg.replaceAll("%teamName%", "")
					.replaceAll("%teamPrefix%", "")
					.replaceAll("%teamSuffix%", "")
					.replaceAll("%teamDisplayName%", "");
		}
		return msg
				.replaceAll("%teamName%", team.getName())
				.replaceAll("%teamPrefix%", team.getPrefix())
				.replaceAll("%teamSuffix%", team.getSuffix())
				.replaceAll("%teamDisplayName%", team.getDisplayName());
	}
	
    public double getRandomDouble(double min, double max){
    	return ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    public int getRandomInteger(int min, int max){
    	return ThreadLocalRandom.current().nextInt(min, max);
    }
    
    public void showActionBarMessage(Player p, String message){
		p.sendActionBar(message);
    }
    
    public void showTitle(Player p, String title, String subtitle){
    	p.sendTitle(title, subtitle, 20, 60, 20);
    }
    
    public void showTitleToAll(String title, String subtitle){
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			showTitle(p, title, subtitle);
		}
    }
    
	public void setVanished(Player p, boolean vanished){
		if(vanished){
			p.setPlayerListName(null);
			p.setCanPickupItems(false);
		}else{
			p.setPlayerListName(p.getName());
			p.setCanPickupItems(true);
		}
		for(Player t : Bukkit.getServer().getOnlinePlayers()){
			if(t.hasPermission(plugin.adminperm)){
				continue;
			}
			if(vanished){
				t.hidePlayer(plugin, p);
			}else{
				t.showPlayer(plugin, p);
			}
		}
	}
}
