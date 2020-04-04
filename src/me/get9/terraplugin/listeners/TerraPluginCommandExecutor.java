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
				sender.sendMessage("§3> Вы не являетесь администратором."); 
				return true;
			}
	    	switch(getArg(args,0)){
	    		case("openinv"):
	    			if(args.length > 1 ){
	    				Player target = Bukkit.getPlayerExact(getArg(args,1));
	    				if(target instanceof Player){
	    					sender.sendMessage("§3> Смотрим в инвентарь игрока: §f" + target.getName());
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
	    					sender.sendMessage("§3> Игрок должен быть онлайн.");
	    				}
	    			}else{
	    				sender.sendMessage("§3> Укажите ник игрока.");
	    			}
				break;
    			
				case("openend"):
					if(args.length > 1 ){
	    					Player target = Bukkit.getPlayerExact(getArg(args,1));
	    					if(target instanceof Player){
	    						sender.sendMessage("§3> Смотрим в эндерсундук игрока: §f" + target.getName());
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
	    						sender.sendMessage("§3> Игрок должен быть онлайн."); 
	    					}
					}else{
						sender.sendMessage("§3> Укажите ник игрока.");
					}
	    		break;
        		
				case("info"):
					if(args.length > 1 ){
						String output = "";
						if(Bukkit.getPlayerExact(getArg(args,1)) != null){
	    					Player target = Bukkit.getPlayerExact(getArg(args,1));
							output = "§3> Информация о игроке: " + target.getName() + "§a[" + target.getAddress() + "]"
									+ "§3\nИграл с/по/онлайн: \n§f- §7" + new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getFirstPlayed())) + "§f / §a" + new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getLastPlayed())) + "§f / §e" + target.getTicksLived()/20/60/60 + "ч"
									+ "§3\nЗдоровье / Воздух / Пища / Уровень: \n§f- §c" + target.getHealth() + "% §f/ §9" + (int)(((double)target.getRemainingAir()/(double)target.getMaximumAir())*100) + "% §f/ §6" + (int)(((double)(target.getFoodLevel()*5D)/100D)*100) + "% §f/ §a" + target.getLevel() + "lvl"
									+ "§3\nКоординаты:\n§f- §f["+target.getLocation().getWorld().getName()+"] " +(int)target.getLocation().getX()+","+(int)target.getLocation().getY()+","+(int)target.getLocation().getZ()
									;
						}else{
							OfflinePlayer target = Bukkit.getOfflinePlayer(getArg(args,1));
							output = "§3> Информация о игроке: " + target.getName() + "§c[Offline]"
									+ "§3\nИграл с/по/онлайн: \n§f- §7" + new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getFirstPlayed())) + "§f / §a" + new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(target.getLastPlayed())) + "§f"
								    ;
						}
						// Check Mods
						if(plugin.getConf().playerMods && plugin.getModsList().playerMods.containsKey(getArg(args,1))){
							TerraPluginPlayerModsRecord re = plugin.getModsList().playerMods.get(getArg(args,1));
							output+="§3\nКлиент: §f" + re.getClientBrand() + "\n§3Моды: §f";
							for(String mod : re.getMods()){
								output+=mod+" ";
							}
						}else{
							output+="\n§cЗаписи о версии клиента отсутствуют.";
						}
						sender.sendMessage(output);
					}else{
						sender.sendMessage("§3> Укажите ник игрока.");
					}
				break;
				
				case("setportal"):
					if(isConsole){
						sender.sendMessage("§3> Команда доступна только игрокам.");
						return true;
					}
					String worldName = ((Player)sender).getLocation().getWorld().getName();
					if(plugin.getConf().portalTargetLocations.containsKey(worldName)){
						plugin.getConf().portalTargetLocations.remove(worldName);
						sender.sendMessage("§3> Точка телепортации для порталов удалена.");
					}else{
						plugin.getConf().portalTargetLocations.put(worldName, plugin.getUtils().locToString(((Player)sender).getLocation()));
						sender.sendMessage("§3> Точка телепортации для порталов задана.");
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
	    					if(!isConsole)sender.sendMessage("§3> Блок активирован.");
    					}
    			break;
    			
    			case("redstop"):
    					if(plugin.getConf().redStoneEnabled){
    						plugin.getConf().redStoneEnabled = false;
        					sender.sendMessage("§3> Весь редстоун остановлен.");
    					}else{
    						plugin.getConf().redStoneEnabled = true;
        					sender.sendMessage("§3> Редстоун активирован.");
    					}
    			break;
    			
    			case("setannounce"):
    					if(isConsole){
    						sender.sendMessage("§3> Команда доступна только игрокам.");
    						return true;
    					}
        				String announceName = sender.getName().toLowerCase();
    					if(plugin.getConf().hideLoginMessage.contains(announceName)){
    						plugin.getConf().hideLoginMessage.remove(announceName);
        					sender.sendMessage("§3> Сообщения о вашем входе/выходе отображены.");
    					}else{
    						plugin.getConf().hideLoginMessage.add(announceName);
        					sender.sendMessage("§3> Сообщения о вашем входе/выходе спрятаны.");
    					}
    			break;
    			
    			case("vanish"):
					if(isConsole){
						sender.sendMessage("§3> Команда доступна только игрокам.");
						return true;
					}
    				String vanishedName = sender.getName().toLowerCase();
					if(plugin.getConf().vanishedPlayers.contains(vanishedName)){
						plugin.getConf().vanishedPlayers.remove(vanishedName);
						plugin.getUtils().setVanished((Player)sender, false);
    					sender.sendMessage("§3> Режим невидимости §cотключен§3.");
					}else{
						plugin.getConf().vanishedPlayers.add(vanishedName);
						plugin.getUtils().setVanished((Player)sender,  true);
    					sender.sendMessage("§3> Режим невидимости §aвключен§3.");
					}
    			break;
    			
    			case("setspeed"):
    					Player target = Bukkit.getPlayerExact(getArg(args,1));
    					if(target instanceof Player){
    	    				if(args.length > 2){
    	    					target.setWalkSpeed(Float.parseFloat(getArg(args,2))/5);
    	    					target.setFlySpeed(Float.parseFloat(args[2])/10);
        						sender.sendMessage("§3> Скорость игрока изменена.");
    	    				}else{
        						sender.sendMessage("§3> Укажите скорость.");
    	    				}
    						
    					}else{
    						sender.sendMessage("§3> Игрок должен быть онлайн.");
    					}
    			break;
    			  			
    			case("help"):
						sender.sendMessage("§3> Список команд:"
								+ "\n §3- Управление игроками:"
								+ "\n §3- /ta openinv ник§f: открывает инвентарь игрока."
								+ "\n §3- /ta openend ник§f: открывает эндерчест игрока."
								+ "\n §3- /ta info ник§f: показывает информацию о игроке."
								+ "\n §3- /ta setspeed ник§f: меняет скорость игрока."
								+ "\n §3- /ta vanish§f: режим невидимости."
								+ "\n §3- /ta setannounce§f: показ сообщений при входе."
								+ "\n §3- Прочие костыли:"
								+ "\n §3- /ta redpower мир x y z§f: размещает блок редстоуна."
								+ "\n §3- /ta redstop§f: отключает редстоун."
								+ "\n §3- /ta setportal§f: установить обратный путь порталов."
								);
    			break;
    			
    			case("reload"):
					sender.sendMessage("§3> Перезагрузка конфигурации.");
    				plugin.reloadConfig();
    			break;
    			
    			case("save"):
					sender.sendMessage("§3> Сохранение конфигурации.");
    				plugin.getConf().saveJsonConfig();
    			break;
    			
    			default:
    				sender.sendMessage("§3> Используйте /ta help для помощи.");
    			break;
    			}
	    } else if(cmd.getName().equalsIgnoreCase("motd")){
			if(isConsole){
				sender.sendMessage("§3> Команда доступна только игрокам.");
				return true;
			}
			sender.sendMessage(plugin.getUtils().formatMessage(String.join("\n",plugin.getConf().motdFormat),(Player) sender));
		}else if(cmd.getName().equalsIgnoreCase("chat")){
			if(isConsole){
				sender.sendMessage("§3> Команда доступна только игрокам.");
			}
			if(plugin.getChat().isLocal(sender.getName())){
				sender.sendMessage("§a> Канал чата изменен на : Глобальный");
				plugin.getChat().setLocal(sender.getName(), false);
			}else{
				sender.sendMessage("§e> Канал чата изменен на : Локальный");
				plugin.getChat().setLocal(sender.getName(), true);
			}
		}else if(cmd.getName().equalsIgnoreCase("bed")){
			if(isConsole){
				sender.sendMessage("§3> Команда доступна только игрокам.");
				return true;
			}
			Player plr = (Player) sender;
			Location bedLoc = plr.getBedSpawnLocation();
			if(bedLoc != null){
				sender.sendMessage("§a> Вы телепортированы к своей кровати.");	
				plr.teleport(bedLoc);
			}else{
				sender.sendMessage("§c> Ваша кровать пропала или доступ к ней затруднен.");	
			}
		}
		return true;
	}
}
