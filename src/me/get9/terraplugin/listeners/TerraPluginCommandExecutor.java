package me.get9.terraplugin.listeners;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import me.get9.terraplugin.TerraPlugin;
import me.get9.terraplugin.mods.playermods.TerraPluginPlayerModsRecord;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TerraPluginCommandExecutor implements CommandExecutor{	
	private TerraPlugin plugin;
	public TerraPluginCommandExecutor (TerraPlugin plugin){
		this.plugin = plugin;
	}
	
	public String getArg(String[] data, int index){
	    try{
	      data[index].length();
	      return data[index];
	    } catch(ArrayIndexOutOfBoundsException e){
	      return "";
	    }
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		boolean isConsole = true;
		if(sender instanceof Player){
			isConsole = false;
		}
	    if (cmd.getName().equalsIgnoreCase("ta")){
			if(!sender.hasPermission(plugin.adminperm)){
				sender.sendMessage(plugin.getLocale("noPermission")); 
				return true;
			}
	    	switch(getArg(args,0)){
	    		case("openinv"):
	    			if(args.length > 1 ){
	    				Player target = Bukkit.getPlayerExact(getArg(args,1));
	    				if(target instanceof Player){
    						sender.sendMessage(plugin.getLocale("openInv", new String[]{target.getName()}));
	    					if(isConsole){
	    						for(ItemStack s : target.getInventory()){
	    							if(s == null) continue;
	    							String enchantments = "";
	    							if(s.getEnchantments().size() > 0){
	    								for(Map.Entry<Enchantment, Integer> entry : s.getEnchantments().entrySet()){
	    									enchantments+=entry.getKey().getName() + ":" + entry.getValue()+",";
	    								}
	    							}
	    							sender.sendMessage("- " + s.getType().toString() + ":" + s.getDurability() + "x" + s.getAmount() + ", " + enchantments);
	    						}
	    					}else{
		    					((Player)sender).openInventory(target.getInventory());
	    					}
	    				}else{
	    					sender.sendMessage(plugin.getLocale("noOnline"));
	    				}
	    			}else{
	    				sender.sendMessage(plugin.getLocale("noNickname"));
	    			}
				break;
    			
				case("openend"):
					if(args.length > 1 ){
	    					Player target = Bukkit.getPlayerExact(getArg(args,1));
	    					if(target instanceof Player){
	    						sender.sendMessage(plugin.getLocale("openEnd", new String[]{target.getName()}));
		    					if(isConsole){
		    						for(ItemStack s : target.getInventory()){
		    							if(s == null) continue;
		    							String enchantments = "";
		    							if(s.getEnchantments().size() > 0){
		    								for(Map.Entry<Enchantment, Integer> entry : s.getEnchantments().entrySet()){
		    									enchantments+=entry.getKey().getName() + ":" + entry.getValue()+",";
		    								}
		    							}
		    							sender.sendMessage("- " + s.getType().toString() + ":" + s.getDurability() + " x" + s.getAmount() + ", " + enchantments);
		    						}
		    					}else{ 
		    						((Player)sender).openInventory(target.getEnderChest()); 
		    					}
	    					}else{
	    						sender.sendMessage(plugin.getLocale("noOnline")); 
	    					}
					}else{
						sender.sendMessage(plugin.getLocale("noNickname"));
					}
	    		break;
        		
				case("info"):
					if(args.length > 1 ){
						String output = "";
						if(Bukkit.getPlayerExact(getArg(args,1)) != null){
	    					Player target = Bukkit.getPlayerExact(getArg(args,1));
							String firstPlayed = new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getFirstPlayed()));
							String lastPlayed = new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getLastLogin()));
							
							output = plugin.getLocale("infoPlayerName", new String[]{target.getName(), target.getAddress().toString()})
									+ plugin.getLocale("infoPlayTime", new String[]{firstPlayed,lastPlayed,Integer.toString(target.getTicksLived()/20/60/60)})
									+ "§3- §c" + target.getHealth() + "❤ §f/ §a"+ target.getLevel() + "lvl"
									+ "\n§3- §e["+target.getLocation().getWorld().getName()+"] " +(int)target.getLocation().getX()+","+(int)target.getLocation().getY()+","+(int)target.getLocation().getZ()
									;
						}else{
							OfflinePlayer target = Bukkit.getOfflinePlayer(getArg(args,1));
							String firstPlayed = new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getFirstPlayed()));
							String lastPlayed = new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getLastLogin()));
							output = plugin.getLocale("infoOffline", new String[]{target.getName(), firstPlayed, lastPlayed});;
						}
						
						// Check Mods
						if(plugin.getConf().playerMods && plugin.getModsList().playerMods.containsKey(getArg(args,1))){
							TerraPluginPlayerModsRecord re = plugin.getModsList().playerMods.get(getArg(args,1));
							output+=plugin.getLocale("infoClient", new String[]{re.getClientBrand()});
						    output+=plugin.getLocale("infoMods");
							for(String mod : re.getMods()){
								output+=mod+" ";
							}
						}
						sender.sendMessage(output);
					}else{
						sender.sendMessage(plugin.getLocale("noNickname"));
					}
				break;
				
				case("setportal"):
					if(isConsole){
						sender.sendMessage(plugin.getLocale("noConsole"));
						return true;
					}
					String worldName = ((Player)sender).getLocation().getWorld().getName();
					if(plugin.getConf().portalTargetLocations.containsKey(worldName)){
						plugin.getConf().portalTargetLocations.remove(worldName);
						plugin.saveConfig();
						sender.sendMessage(plugin.getLocale("portalDeleted"));
					}else{
						plugin.getConf().portalTargetLocations.put(worldName, plugin.getUtils().locToString(((Player)sender).getLocation()));
						plugin.saveConfig();
						sender.sendMessage(plugin.getLocale("portalCreated"));
					}
				break;

    			case("redpower"):
    					if(args.length == 5){
	    					final Block bl = Bukkit.getWorld(getArg(args,1)).getBlockAt(Integer.parseInt(getArg(args,2)), Integer.parseInt(getArg(args,3)), Integer.parseInt(getArg(args,4)));
	    					bl.setType(Material.REDSTONE_BLOCK);
	    					plugin.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	    						@Override
	    						public void run() {
	    							bl.setType(Material.EMERALD_BLOCK);
	    						}
	    					},20L);
    						sender.sendMessage(plugin.getLocale("redstoneToggled"));
    					}
    			break;
    			
    			case("redstop"):
    					if(plugin.getConf().redStoneEnabled){
    						plugin.getConf().redStoneEnabled = false;
    						sender.sendMessage(plugin.getLocale("redstoneOff"));
    					}else{
    						plugin.getConf().redStoneEnabled = true;
    						sender.sendMessage(plugin.getLocale("redstoneOn"));
    					}
    			break;
    			
    			case("setannounce"):
    					if(isConsole){
    						sender.sendMessage(plugin.getLocale("noConsole"));
    						return true;
    					}
        				String announceName = sender.getName().toLowerCase();
    					if(plugin.getConf().hideLoginMessage.contains(announceName)){
    						plugin.getConf().hideLoginMessage.remove(announceName);
    						sender.sendMessage(plugin.getLocale("announceOn")); 

    					}else{
    						plugin.getConf().hideLoginMessage.add(announceName);
    						sender.sendMessage(plugin.getLocale("announceOff")); 
    					}
    			break;
    			
    			case("vanish"):
					if(isConsole){
						sender.sendMessage(plugin.getLocale("noConsole"));
						return true;
					}
    				String vanishedName = sender.getName().toLowerCase();
					if(plugin.getConf().vanishedPlayers.contains(vanishedName)){
						plugin.getConf().vanishedPlayers.remove(vanishedName);
						plugin.getUtils().setVanished((Player)sender, false);
						sender.sendMessage(plugin.getLocale("vanishOff")); 
					}else{
						plugin.getConf().vanishedPlayers.add(vanishedName);
						plugin.getUtils().setVanished((Player)sender,  true);
						sender.sendMessage(plugin.getLocale("vanishOn")); 
					}
    			break;
    			
    			case("setspeed"):
    					Player target = Bukkit.getPlayerExact(getArg(args,1));
    					if(target instanceof Player){
    	    				if(args.length > 2){
    	    					target.setWalkSpeed(Float.parseFloat(getArg(args,2))/5);
    	    					target.setFlySpeed(Float.parseFloat(args[2])/10);
        						sender.sendMessage(plugin.getLocale("speedSet")); 
    	    				}else{
        						sender.sendMessage(plugin.getLocale("speedNotValid")); 
    	    				}
    						
    					}else{
    						sender.sendMessage(plugin.getLocale("noOnline")); 
    					}
    			break;
    			  			
    			case("help"):
					sender.sendMessage(plugin.getLocale("helpShow")); 
    			break;
    			
    			case("reload"):
					sender.sendMessage(plugin.getLocale("configReload"));
    				plugin.reloadConfig();
    			break;
    			
    			case("save"):
					sender.sendMessage(plugin.getLocale("configSave"));
    				plugin.saveConfig();
    			break;
    			
    			default:
    				sender.sendMessage(plugin.getLocale("helpUse"));
    			break;
    			}
	    } else if(cmd.getName().equalsIgnoreCase("motd")){
			if(isConsole){
				sender.sendMessage(plugin.getLocale("noConsole"));
				return true;
			}
			sender.sendMessage(plugin.getUtils().formatMessage(String.join("\n",plugin.getConf().motdFormat),(Player) sender));
		}else if(cmd.getName().equalsIgnoreCase("chat")){
			if(isConsole){
				sender.sendMessage(plugin.getLocale("noConsole"));
				return true;
			}
			if(plugin.getChat().isLocal(sender.getName().toLowerCase())){
				sender.sendMessage(plugin.getLocale("chatGlobal"));
				plugin.getChat().setLocal(sender.getName().toLowerCase(), false);
			}else{
				sender.sendMessage(plugin.getLocale("chatLocal"));
				plugin.getChat().setLocal(sender.getName().toLowerCase(), true);
			}
		}else if(cmd.getName().equalsIgnoreCase("bed")){
			if(isConsole){
				sender.sendMessage(plugin.getLocale("noConsole"));
				return true;
			}
			Player plr = (Player) sender;
			Location bedLoc = plr.getBedSpawnLocation();
			if(bedLoc != null){
				sender.sendMessage(plugin.getLocale("bedTeleported"));
				plr.teleport(bedLoc);
			}else{
				sender.sendMessage(plugin.getLocale("bedDeleted"));
			}
		}
		return true;
	}
}
