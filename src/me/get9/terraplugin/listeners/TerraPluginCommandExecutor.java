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
				sender.sendMessage("�3> �� �� ��������� ���������������."); 
				return true;
			}
	    	switch(getArg(args,0)){
	    		case("openinv"):
	    			if(args.length > 1 ){
	    				Player target = Bukkit.getPlayerExact(getArg(args,1));
	    				if(target instanceof Player){
	    					sender.sendMessage("�3> ������� � ��������� ������: �f" + target.getName());
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
	    					sender.sendMessage("�3> ����� ������ ���� ������.");
	    				}
	    			}else{
	    				sender.sendMessage("�3> ������� ��� ������.");
	    			}
				break;
    			
				case("openend"):
					if(args.length > 1 ){
	    					Player target = Bukkit.getPlayerExact(getArg(args,1));
	    					if(target instanceof Player){
	    						sender.sendMessage("�3> ������� � ����������� ������: �f" + target.getName());
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
	    						sender.sendMessage("�3> ����� ������ ���� ������."); 
	    					}
					}else{
						sender.sendMessage("�3> ������� ��� ������.");
					}
	    		break;
        		
				case("info"):
					if(args.length > 1 ){
						String output = "";
						if(Bukkit.getPlayerExact(getArg(args,1)) != null){
	    					Player target = Bukkit.getPlayerExact(getArg(args,1));
							output = "�3> ���������� � ������: " + target.getName() + "�a[" + target.getAddress() + "]"
									+ "�3\n����� �/��/������: \n�f- �7" + new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getFirstPlayed())) + "�f / �a" + new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getLastPlayed())) + "�f / �e" + target.getTicksLived()/20/60/60 + "�"
									+ "�3\n�������� / ������ / ���� / �������: \n�f- �c" + target.getHealth() + "% �f/ �9" + (int)(((double)target.getRemainingAir()/(double)target.getMaximumAir())*100) + "% �f/ �6" + (int)(((double)(target.getFoodLevel()*5D)/100D)*100) + "% �f/ �a" + target.getLevel() + "lvl"
									+ "�3\n����������:\n�f- �f["+target.getLocation().getWorld().getName()+"] " +(int)target.getLocation().getX()+","+(int)target.getLocation().getY()+","+(int)target.getLocation().getZ()
									;
						}else{
							OfflinePlayer target = Bukkit.getOfflinePlayer(getArg(args,1));
							output = "�3> ���������� � ������: " + target.getName() + "�c[Offline]"
									+ "�3\n����� �/��/������: \n�f- �7" + new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getFirstPlayed())) + "�f / �a" + new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getLastPlayed())) + "�f"
								    ;
						}
						// Check Mods
						if(plugin.getConf().playerMods && plugin.getModsList().playerMods.containsKey(getArg(args,1))){
							TerraPluginPlayerModsRecord re = plugin.getModsList().playerMods.get(getArg(args,1));
							output+="�3\n������: �f" + re.getClientBrand() + "\n�3����: �f";
							for(String mod : re.getMods()){
								output+=mod+" ";
							}
						}else{
							output+="\n�c������ � ������ ������� �����������.";
						}
						sender.sendMessage(output);
					}else{
						sender.sendMessage("�3> ������� ��� ������.");
					}
				break;
				
				case("setportal"):
					if(isConsole){
						sender.sendMessage("�3> ������� �������� ������ �������.");
						return true;
					}
					String worldName = ((Player)sender).getLocation().getWorld().getName();
					if(plugin.getConf().portalTargetLocations.containsKey(worldName)){
						plugin.getConf().portalTargetLocations.remove(worldName);
						sender.sendMessage("�3> ����� ������������ ��� �������� �������.");
					}else{
						plugin.getConf().portalTargetLocations.put(worldName, plugin.getUtils().locToString(((Player)sender).getLocation()));
						sender.sendMessage("�3> ����� ������������ ��� �������� ������.");
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
	    					if(!isConsole)sender.sendMessage("�3> ���� �����������.");
    					}
    			break;
    			
    			case("redstop"):
    					if(plugin.getConf().redStoneEnabled){
    						plugin.getConf().redStoneEnabled = false;
        					sender.sendMessage("�3> ���� �������� ����������.");
    					}else{
    						plugin.getConf().redStoneEnabled = true;
        					sender.sendMessage("�3> �������� �����������.");
    					}
    			break;
    			
    			case("setannounce"):
    					if(isConsole){
    						sender.sendMessage("�3> ������� �������� ������ �������.");
    						return true;
    					}
        				String announceName = sender.getName().toLowerCase();
    					if(plugin.getConf().hideLoginMessage.contains(announceName)){
    						plugin.getConf().hideLoginMessage.remove(announceName);
        					sender.sendMessage("�3> ��������� � ����� �����/������ ����������.");
    					}else{
    						plugin.getConf().hideLoginMessage.add(announceName);
        					sender.sendMessage("�3> ��������� � ����� �����/������ ��������.");
    					}
    			break;
    			
    			case("vanish"):
					if(isConsole){
						sender.sendMessage("�3> ������� �������� ������ �������.");
						return true;
					}
    				String vanishedName = sender.getName().toLowerCase();
					if(plugin.getConf().vanishedPlayers.contains(vanishedName)){
						plugin.getConf().vanishedPlayers.remove(vanishedName);
						plugin.getUtils().setVanished((Player)sender, false);
    					sender.sendMessage("�3> ����� ����������� �c��������3.");
					}else{
						plugin.getConf().vanishedPlayers.add(vanishedName);
						plugin.getUtils().setVanished((Player)sender,  true);
    					sender.sendMessage("�3> ����� ����������� �a�������3.");
					}
    			break;
    			
    			case("setspeed"):
    					Player target = Bukkit.getPlayerExact(getArg(args,1));
    					if(target instanceof Player){
    	    				if(args.length > 2){
    	    					target.setWalkSpeed(Float.parseFloat(getArg(args,2))/5);
    	    					target.setFlySpeed(Float.parseFloat(args[2])/10);
        						sender.sendMessage("�3> �������� ������ ��������.");
    	    				}else{
        						sender.sendMessage("�3> ������� ��������.");
    	    				}
    						
    					}else{
    						sender.sendMessage("�3> ����� ������ ���� ������.");
    					}
    			break;
    			  			
    			case("help"):
						sender.sendMessage("�3> ������ ������:"
								+ "\n �3- ���������� ��������:"
								+ "\n �3- /ta openinv ���f: ��������� ��������� ������."
								+ "\n �3- /ta openend ���f: ��������� ��������� ������."
								+ "\n �3- /ta info ���f: ���������� ���������� � ������."
								+ "\n �3- /ta setspeed ���f: ������ �������� ������."
								+ "\n �3- /ta vanish�f: ����� �����������."
								+ "\n �3- /ta setannounce�f: ����� ��������� ��� �����."
								+ "\n �3- ������ �������:"
								+ "\n �3- /ta redpower ��� x y z�f: ��������� ���� ���������."
								+ "\n �3- /ta redstop�f: ��������� ��������."
								+ "\n �3- /ta setportal�f: ���������� �������� ���� ��������."
								);
    			break;
    			
    			case("reload"):
					sender.sendMessage("�3> ������������ ������������.");
    				plugin.reloadConfig();
    			break;
    			
    			case("save"):
					sender.sendMessage("�3> ���������� ������������.");
    				plugin.getConf().saveJsonConfig();
    			break;
    			
    			default:
    				sender.sendMessage("�3> ����������� /ta help ��� ������.");
    			break;
    			}
	    } else if(cmd.getName().equalsIgnoreCase("motd")){
			if(isConsole){
				sender.sendMessage("�3> ������� �������� ������ �������.");
				return true;
			}
			sender.sendMessage(plugin.getUtils().formatMessage(String.join("\n",plugin.getConf().motdFormat),(Player) sender));
		}else if(cmd.getName().equalsIgnoreCase("chat")){
			if(isConsole){
				sender.sendMessage("�3> ������� �������� ������ �������.");
			}
			if(plugin.getChat().isLocal(sender.getName())){
				sender.sendMessage("�a> ����� ���� ������� �� : ����������");
				plugin.getChat().setLocal(sender.getName(), false);
			}else{
				sender.sendMessage("�e> ����� ���� ������� �� : ���������");
				plugin.getChat().setLocal(sender.getName(), true);
			}
		}else if(cmd.getName().equalsIgnoreCase("bed")){
			if(isConsole){
				sender.sendMessage("�3> ������� �������� ������ �������.");
				return true;
			}
			Player plr = (Player) sender;
			Location bedLoc = plr.getBedSpawnLocation();
			if(bedLoc != null){
				sender.sendMessage("�a> �� ��������������� � ����� �������.");	
				plr.teleport(bedLoc);
			}else{
				sender.sendMessage("�c> ���� ������� ������� ��� ������ � ��� ���������.");	
			}
		}
		return true;
	}
}
