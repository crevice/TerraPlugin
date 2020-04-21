package me.get9.terraplugin.listeners;


import java.util.List;

import me.get9.terraplugin.TerraPlugin;
import me.get9.terraplugin.mods.randomoredrops.TerraPluginRandomOre;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class TerraPluginListener implements Listener{	
	private TerraPlugin plugin;
	public TerraPluginListener(TerraPlugin plugin){
		this.plugin = plugin;
	}
	
	/*
	 *  PLAYER INTERACT SOMETHING
	 */
	// ToDo: use useInteractedBlock, instead of deprecated isCancelled method.
	@EventHandler
	public void onPlayerInteract (PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		// RandomOre: Check Ore Left In Chunk.
		if(plugin.getConf().randomOreDrops){
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK ){
				Player plr = event.getPlayer();
				if(Material.CLOCK.equals(event.getMaterial())){
					plugin.getUtils().showActionBarMessage(plr, plugin.getRandomOreDrops().showOreInfo(event.getClickedBlock().getLocation()));
					plr.setCooldown(event.getMaterial(), 100);
					plr.playSound(plr.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
				}
			}
		}
	}
	
	/*
	 * PLAYER TOGGLE ELYTRA FLIGHT
	 */
	@EventHandler
	public void onElytraToggle(EntityToggleGlideEvent event){
		if(event.isCancelled()) return;
		// Sometimes we need to nerf Elytra...
		if(plugin.getConf().elytra && event.isGliding()){
			if(plugin.getConf().elytraDisabledWorlds.contains(event.getEntity().getWorld().getName())){
				event.setCancelled(true);
			}
		}
	}

	/*
	 * BLOCK BREAKED
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()) return;
		// Random Ore Drops
		if(plugin.getConf().randomOreDrops){
			Player plr = event.getPlayer();
			Block block = event.getBlock();
			// If Break Randomizer block
			for(TerraPluginRandomOre ore : plugin.getConf().randomOreDropsOres){
				if(ore.type.equalsIgnoreCase(block.getType().toString())){
					event.setDropItems(false);
					event.setExpToDrop(0);
					plr.sendMessage("§c> Данный ресурс рандомно дропается из камня.");
					break;
				}
			}
			// Else, Dice Roll
			if(block.getType().equals(Material.STONE)){
				TerraPluginRandomOre ore = plugin.getRandomOreDrops().getRandomOre(plr, block.getLocation());
				if(ore != null){
					ItemStack oreDrops = new ItemStack(Material.getMaterial(ore.type));
					plr.getWorld().dropItem(block.getLocation(),  oreDrops);
					event.setExpToDrop((int) ore.xp);
					// Play Bell Sound
					plr.playSound(block.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, (float) (1+plugin.getUtils().getRandomDouble(-0.1, 0.3)));
					// Send Message In ACTION BAR
					plugin.getUtils().showActionBarMessage(plr, "§aДжекпот! Из камня выпала руда!");
				}
			}
		}		
	}
	
	/*
	 * PLAYER SEND MESSAGE TO CHAT
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled()) return;
		if (plugin.getConf().chat){
			plugin.getChat().sendChatMessage(event.getMessage(), event.getPlayer());
			event.setCancelled(true);
		}
    }
	
	/*
	 * PORTAL ITEM TRAVEL
	 */
	@EventHandler
	public void onEntityPortalTravel(EntityPortalEvent event){
		if(event.isCancelled()) return;
		// Entity Jumps in portal
		if(plugin.getConf().entityPortalTravel){
			if(plugin.getConf().entityPortalTravelWorlds.contains(event.getFrom().getWorld().getName().toLowerCase())){
				event.setCancelled(true);
			}
		}
	}
	
	/*
	 * PORTAL CREATION IN WORLDS
	 */
	@EventHandler
	public void onPlayerPortalTravel(PlayerPortalEvent event){
		if(event.isCancelled()) return;
		// Portal Spawn Event, deny creation of obsidian frame and teleport player to defined spot
		if(plugin.getConf().portalTarget){
			if(plugin.getConf().portalTargetLocations.containsKey(event.getTo().getWorld().getName())){
				event.setSearchRadius(0);
				event.setCanCreatePortal(false);
				event.setTo(plugin.getUtils().stringToLoc(plugin.getConf().portalTargetLocations.get(event.getTo().getWorld().getName())));
			}
		}
	}
	
	/*
	 * ENTITY PRIMED TO EXPLODE
	 */
	@EventHandler
	public void onExplosion(ExplosionPrimeEvent event){
		if(event.isCancelled()) return;
		// Booooost entity explosion radius
		if(plugin.getConf().explosions && plugin.getConf().explosionsRadiusMultEntities.contains(event.getEntityType().toString())){
			event.setRadius(event.getRadius()*plugin.getConf().explosionsRadiusMult);	
		}
	}
	
	/*
	 * WHEN ENTITY EXPLODE, CREEPER, ETC
	 */
	@EventHandler
	public void onEntityExplosion(EntityExplodeEvent event){
		if(event.isCancelled()) return;
		// Deal no damage to blocks, if enabled
		if(plugin.getConf().explosions && plugin.getConf().explosionsNoDamageBlocks){
			event.blockList().clear();
		}
	}
	
	/*
	 * WHEN PLAYER TELEPORTS
	 */
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if(event.isCancelled()) return;
		// Disable Enderpearl usage, spawn VERY ANGRY Enderman or Endermite on telepoint if enabled. Pikachu, i choose you!
		if(plugin.getConf().itemTeleport){
			if(plugin.getConf().itemTeleportReasons.contains(event.getCause().toString()) && plugin.getConf().itemTeleportDisableInWorlds.contains(event.getTo().getWorld().getName().toLowerCase())){
				if(plugin.getConf().itemTeleportSpawnEnders){
					//Select Monster
					Monster pearlMon;
					if(plugin.getUtils().getRandomInteger(0, 100) < 30){
						pearlMon = (Monster) event.getTo().getWorld().spawnEntity(event.getTo(), EntityType.ENDERMAN);	
					}else{
						pearlMon = (Monster) event.getTo().getWorld().spawnEntity(event.getTo(), EntityType.ENDERMITE);
					}
					// Play Effect
					event.getTo().getWorld().playSound(event.getTo(), Sound.BLOCK_END_GATEWAY_SPAWN, 1, 1.2F);
					// Make Monster Angry to nearest player
					for(Entity t: pearlMon.getNearbyEntities(2, 2, 2)){
						if(t instanceof Player){
							pearlMon.setTarget((LivingEntity) t);
							break;
						}
					}
				}
				event.setCancelled(true);
			}
		}
	}
	
	/*
	 * WHEN MOB SPAWNS
	 */
	@EventHandler
    public void onEntitySpawned(CreatureSpawnEvent event){
		if(event.isCancelled()) return;
		LivingEntity ent = event.getEntity();
		String entType = ent.getType().toString();
		
		// Fix for Chicken Jockeys Mount won't de-spawn naturally.
		if(entType.equals("CHICKEN")){
			Block spawnBlock =  ent.getLocation().getBlock();
			if(spawnBlock.getLightLevel() < 8 && !spawnBlock.getType().equals(Material.GRASS)){
				ent.setRemoveWhenFarAway(true);
			}
		}
		
		// Fix For Skeletal Horses won't de-spawn naturally.
		if(entType.equals("SKELETON_HORSE")){
			ent.setRemoveWhenFarAway(true);
		}
		
		// TAG Entities Spawned From MobSpawner
		if(plugin.getConf().mobSpawners && event.getSpawnReason() == SpawnReason.SPAWNER){
		    if(plugin.getConf().mobSpawnersWorlds.contains(event.getLocation().getWorld().getName()) && plugin.getConf().mobSpawnersEntities.contains(entType)){
		    	ent.setMetadata("fromSpawner", new FixedMetadataValue(plugin,true));
		    }
		}
		
		// Full Moon Event, mob spawn
		if(plugin.getConf().fullMoon && plugin.getFullMoon().running){
			switch(event.getEntityType().toString()){
				case("ZOMBIE"):
					if(plugin.getUtils().getRandomInteger(0,3) == 0){
						event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.ZOMBIE_VILLAGER);
						event.setCancelled(true);
					}
					ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8000, 0));
					ent.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 8000, 4));
					ent.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 8000, 0));
				break;
				case("SKELETON"):
					ent.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 8000, 4));
				break;
				case("CREEPER"):
					ent.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 8000, 1));
				break;
				case("SPIDER"):
					ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8000, 1));
					ent.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 8000, 0));
				break;
				case("CAVE_SPIDER"):
					ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8000, 1));
					ent.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 6000, 0));
				break;
				case("ZOMBIE_VILLAGER"):
					ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8000, 1));
					ent.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 8000, 4));
					ent.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 8000, 2));
					ent.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 8000, 0));
					ent.getEquipment().setItemInMainHand(new ItemStack(Material.STONE_AXE));
				break;
			}
		}
		
		// Make Some Mobs More Healthy
		if(plugin.getConf().customMobs){
			if(plugin.getConf().customMobsHealth.containsKey(entType)){
				ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(plugin.getConf().customMobsHealth.get(entType));
				ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
			}
		}
    }

	/*
	 * ENTITY TAKE DAMAGE FROM ANYWHERE
	 */
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent event){
		if(event.isCancelled()) return;
		
		// Remove some time from Invisibility When Player When Damaged
		if(plugin.getConf().invisibilityPotions){
	    	if((event.getEntity() instanceof Player)){
	    		Player plr = (Player) event.getEntity();
	    		PotionEffect inviz = plr.getPotionEffect(PotionEffectType.INVISIBILITY);
	    		if(inviz != null){
	    			plr.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int)(inviz.getDuration() - plugin.getConf().invisibilityPotionsDamagePenalty*20*event.getDamage()), 0));
	    		}
	    	}
		}
	}
	
	/*
	 * ENTITY TAKE DAMAGE FROM ENTITY
	 */
	@EventHandler
    public void onEntityTakeDamage(EntityDamageByEntityEvent event){
		if(event.isCancelled()) return;
		
		// Apply Timer on Damaged Player
		if(plugin.getConf().damageTimer){
	    	if(event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity){
	    		if(plugin.getConf().damageTimerCause.contains(event.getCause().toString())){
	    			plugin.getConf().damageTimerLastRecieved.put(event.getEntity().getName().toLowerCase(), System.currentTimeMillis()/1000);
	    		}
	    	}
		}
		
		// If Entity Dead By Spectral Arrow
		if(plugin.getConf().arrows && plugin.getConf().arrowsSpectralMoveDrops){
			if(event.getCause().equals(DamageCause.PROJECTILE)){
				if(event.getDamager() instanceof SpectralArrow){
					SpectralArrow arrow = (SpectralArrow) event.getDamager();
					if(arrow.getShooter() instanceof Player && event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof Player)){
						event.getEntity().setMetadata("sparr", new FixedMetadataValue(plugin, ((Player) arrow.getShooter()).getName()));
						((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,200,0));
					}
				}
			}
		}
    }
	
	/*
	 * ENTITY DIES, NOOOOOOOOOOOOOOOOOOOOOOOO
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event){
		LivingEntity ent = event.getEntity();
		String entType = ent.getType().toString();
		
		// Need reworks, make configurable. Deletes Skeleton Horses...
		if(entType.equals("ZOMBIE") || entType.equals("SKELETON")){
			if(ent.getVehicle() != null){
				String vehType = ent.getVehicle().getType().toString();
				if(vehType.equals("CHICKEN") || vehType.equals("SKELETON_HORSE")){
					((LivingEntity)event.getEntity().getVehicle()).damage(100);
				}
			}
		}
		
		// Drop More Loot, when FullMoon
		if(plugin.getConf().fullMoon && plugin.getFullMoon().running){
			if(event.getEntity() instanceof Monster){
				event.setDroppedExp((int)(event.getDroppedExp()*plugin.getConf().fullMoonExpDropMult));
				List<ItemStack> drops = event.getDrops();
				for(ItemStack d : drops){
					if(d.getAmount()*plugin.getConf().fullMoonDropMult < d.getMaxStackSize()){
						d.setAmount((int)(d.getAmount()*plugin.getConf().fullMoonDropMult));
					}
				}
			}
		}
		
		// Nerf loot From Mob Spawners.
		if(plugin.getConf().mobSpawners){
	    	if(event.getEntity() instanceof LivingEntity && event.getEntity().hasMetadata("fromSpawner")){
	    		// Drop Items with chance (30%-Default)
	    		if(plugin.getUtils().getRandomInteger(0, 100) > plugin.getConf().mobSpawnersDropChance){
	    			event.getDrops().clear();
	    		}
	    		event.setDroppedExp(plugin.getConf().mobSpawnersExpMult*event.getDroppedExp());
	    	}
		}
		
		// If Killed By Spectral Arrow From Player
		if(plugin.getConf().arrows && plugin.getConf().arrowsSpectralMoveDrops){
			if(event.getEntity().hasMetadata("sparr")){
				if(event.getEntity().hasPotionEffect(PotionEffectType.GLOWING)){
					Player hunter = plugin.getServer().getPlayer(event.getEntity().getMetadata("sparr").get(0).asString());
					// Check if player Exist, Slavs can leave server when entity is dying.
					if(hunter != null){
						// Give Items to Player
						for(ItemStack i : event.getDrops()){
							if(hunter.getInventory().firstEmpty() == -1){
								hunter.getWorld().dropItem(hunter.getLocation(), i);
							}else{
								hunter.getInventory().addItem(i);
							}
						}
						// Give Player Exp
						hunter.giveExp(event.getDroppedExp());
						
						// No Need to Drop on Entity
						event.setDroppedExp(0);
		    			event.getDrops().clear();
					}
				}
			}
		}
    }

	/*
	 * WHEN PLAYER MOVES
	 */
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (e.isCancelled()) return;
		// Don't Count Player Head Movement
	    if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockY() == e.getFrom().getBlockY() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) return;
	    
	    // Restrict vertical movement out of world, useful to block Nether Roof Walking 
	    if (plugin.getConf().restrictHeight){
	    	if(plugin.getConf().restrictHeightLimits.containsKey(e.getTo().getWorld().getName())){
	    		int limit = plugin.getConf().restrictHeightLimits.get(e.getTo().getWorld().getName());
		    	if((limit > 0 && e.getTo().getY() > limit) || (limit < 0 && e.getTo().getY() < limit)){
		    		// Damage Player
		    		e.getPlayer().damage(2);
		    	}
	    	}
		}
	}
	
	/*
	 * PLAYERS LEAVE SERVER, TOO SAD...
	 */
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		String plrName = event.getPlayer().getName().toLowerCase();
		
		// Set Quit Message
		if (plugin.getConf().chat){
			event.setQuitMessage(plugin.getUtils().formatMessage(plugin.getConf().chatLeaveServer, event.getPlayer()));
		}
		
		// Hide Quit Message for hidden players
		if(plugin.getConf().hideLoginMessage.contains(plrName)){
			event.setQuitMessage(null);
		}
		
		// If Player Leaves in battle, apply penalty on next enter...
		if(plugin.getConf().damageTimer){
			if(plugin.getConf().damageTimerLastRecieved.containsKey(plrName)){
				Long time = System.currentTimeMillis()/1000;
				// Remove Record if interval exceeded.
				if(plugin.getConf().damageTimerLastRecieved.get(plrName) + plugin.getConf().damageTimerInterval < time){
					plugin.getConf().damageTimerLastRecieved.remove(plrName, time);
				}
			}
		}
	}
	
	/*
	 * PLAYER KICKED FROM SERVER
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onKick(PlayerKickEvent event){
		if (event.isCancelled()) return;
		
		// Set Quit Message, When Kicked
		if (plugin.getConf().chat){
			event.setLeaveMessage(plugin.getUtils().formatMessage(plugin.getConf().chatLeaveServer, event.getPlayer()));
		}
	}
	
	/*
	 * PLAYER LOGS IN ON SERVER
	 */
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event){
		// Queue FML Mods List Request
	    // Broken in (1.13) or (1.15.2) due to Forge Rewrite, Recommend to Turn Off PlayerMods
		/*if(plugin.getConf().playerMods){
			plugin.getModsList().clearRecord(event.getPlayer().getName());
			plugin.getModsList().requestFMLModsList(event.getPlayer());
		}*/
	}
    
	/*
	 * PLAYER JOINED ON SERVER, STARTING TO LOADING WORLD ETC
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player plr = event.getPlayer();
		String plrName = plr.getName().toLowerCase();
		
		// Set Join Message
		if (plugin.getConf().chat){
			event.setJoinMessage(plugin.getUtils().formatMessage(plugin.getConf().chatJoinedServer, plr));
		}
		
		// Hide Join Message for hidden Players
		if(plugin.getConf().hideLoginMessage.contains(plrName)){
			event.setJoinMessage(null);
		}
		
		// Vanish player
		if(plugin.getConf().vanishedPlayers.contains(plrName)){
			plugin.getUtils().setVanished(plr, true);
		}
		
		//Check for Restricted Mods
		if(plugin.getConf().playerMods){
			plugin.getModsList().queueCheckDisabledMods(plrName);
		}
		
		// Hide Online Vanished players for Joined
		if(!plr.hasPermission(plugin.adminperm)){
			for(String vanishedName : plugin.getConf().vanishedPlayers){
				Player vP = Bukkit.getServer().getPlayerExact(vanishedName);
				if(vP != null) {
					plr.hidePlayer(plugin, vP);
				}
			}
		}
		
		//Broadcast Active Events
		if(plugin.getConf().fullMoon && plugin.getFullMoon().running){
			plugin.getUtils().showTitle(plr, "§9Полнолуние!", "Монстры сильнее, а добыча богаче!");
		}
		
		// Send Motd
		if(plugin.getConf().motd){
			plr.sendMessage(plugin.getUtils().formatMessage(String.join("\n",plugin.getConf().motdFormat),plr));
		}
		
		// Apply Penalty for leave in Battle.
		if(plugin.getConf().damageTimer){
			if(plugin.getConf().damageTimerLastRecieved.containsKey(plrName)){
				plr.sendMessage("§c> Не покидайте игру во время боя!");
				plr.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
				plr.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 0));
				plugin.getConf().damageTimerLastRecieved.remove(plrName);
			}
		}
	}
	
	/*
	 * WHEN PLAYER DIES: PRESS F TO PAY RESPECTS
	 */
    @EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
    	Player player = event.getEntity();
    	
    	// If player dies near bed, mark bedspawn unsafe
    	if(plugin.getConf().bedSpawnProtection){
    	    if(player.getBedSpawnLocation() != null && player.getBedSpawnLocation().getWorld().getName().equals(player.getLocation().getWorld().getName())) {
    	    	if(player.getLocation().distance(player.getBedSpawnLocation()) < plugin.getConf().bedSpawnProtectionRange){
    		    	plugin.getConf().bedSpawnProtectionDead.add(player.getName().toLowerCase());
    	    	}
    	    }
    	}
    	
    	//Drop Items On Death, if world gamerule keepInventory is false, dont run!!!
    	if(plugin.getConf().dropItems && "true".equals(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY))){
			PlayerInventory inv = player.getInventory();
			Location loc = player.getLocation();
			
    		// Remove Some XP
    		if(player.getLevel() > 0){
    			player.setExp(0);
        		player.setLevel((int)(player.getLevel() * plugin.getConf().dropItemsLevelPercent));
    		}
    		
    		//Hand Right 
    		if(inv.getItemInMainHand() != null){
    			if(plugin.getConf().dropItemsList.contains(inv.getItemInMainHand().getType().toString()) || inv.getItemInMainHand().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)){
    				player.getWorld().dropItem(loc, inv.getItemInMainHand());
    				inv.setItemInMainHand(null);
    			}
    		}
    		
    		//Hand Left
    		if(inv.getItemInOffHand() != null){
    			if(plugin.getConf().dropItemsList.contains(inv.getItemInOffHand().getType().toString()) || inv.getItemInOffHand().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)){
    				player.getWorld().dropItem(loc, inv.getItemInOffHand());
    				inv.setItemInOffHand(null);
    			}
    		}
    		
    		//Helmet
    		if(inv.getHelmet() != null){
    			if(plugin.getConf().dropItemsList.contains(inv.getHelmet().getType().toString()) || inv.getHelmet().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)){
    				player.getWorld().dropItem(loc, inv.getHelmet());
    				inv.setHelmet(null);
    			}
    		}
    		
    		//Chestplate
    		if(inv.getChestplate() != null){
    			if(plugin.getConf().dropItemsList.contains(inv.getChestplate().getType().toString()) || inv.getChestplate().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)){
    				player.getWorld().dropItem(loc, inv.getChestplate());
    				inv.setChestplate(null);
    			}
    		}
    		
    		//Leggings
    		if(inv.getLeggings() != null){
    			if(plugin.getConf().dropItemsList.contains(inv.getLeggings().getType().toString()) || inv.getLeggings().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)){
    				player.getWorld().dropItem(loc, inv.getLeggings());
    	    		inv.setLeggings(null);
    			}
    		}
    		//Boots
    		if(inv.getBoots() != null){
    			if(plugin.getConf().dropItemsList.contains(inv.getBoots().getType().toString()) || inv.getBoots().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)){
    				player.getWorld().dropItem(loc, inv.getBoots());
    	    		inv.setBoots(null);
    			}
    		}
        	
    		//Inventory
    		for(ItemStack i : inv.getContents()){
    			if(i != null){
	    			if(plugin.getConf().dropItemsList.contains(i.getType().toString()) || i.getEnchantments().containsKey(Enchantment.VANISHING_CURSE)){
	    				player.getWorld().dropItem(loc, i);
	    				inv.remove(i);
	    			}
    			}
    		}
    	}
    }
    
    /*
     * ON PLAYER RESPAWNED
     */
    @EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent event){
    	// If Bedroom unsafe, spawn in another place
    	if(plugin.getConf().bedSpawnProtection){
        	Player plr = event.getPlayer();
        	String plrName = plr.getName().toLowerCase();
        	// Check If Player dies near bed
        	if(plugin.getConf().bedSpawnProtectionDead.contains(plrName)){
            	event.setRespawnLocation(plr.getWorld().getSpawnLocation());
            	plugin.getConf().bedSpawnProtectionDead.remove(plrName);
            	plr.sendMessage("§b> Возрождаемся на спавне, спальня не безопасна!");
        	}
    	}
    }
	
    /*
     * DISPENSER SHOOTING SHIT, PEWPEWPEW!
     */
    @EventHandler
    public void onProjectileLanuched(ProjectileLaunchEvent event){
    	if(event.isCancelled()) return;
    	if(plugin.getConf().boostDispenser){
        	if (!(event.getEntity().getShooter() instanceof LivingEntity) && !event.getEntityType().equals(EntityType.SMALL_FIREBALL)){
        		event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(plugin.getConf().boostDispenserMult));
        	}
    	}
    	
    	// Disable Pickup for Tipped Arrows
    	if(plugin.getConf().arrows && !plugin.getConf().arrowsCanPickupTipped){
	    	if(event.getEntityType().equals(EntityType.ARROW)){
	        	Arrow arrow = (Arrow) event.getEntity();
	        	// Don't Pickup Arrows, that haz potioneffects
	        	if(!PotionType.UNCRAFTABLE.equals(arrow.getBasePotionData().getType())){
		            arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
	        	}
	    	}
    	}
    }
    
    /*
     * PISTON MOVE BLOCK FORWARD, WOAH!
     */
	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent event) {
    	if(event.isCancelled()) return;
    	// Cancel if piston moves restricted blocks FORWARD
		if(plugin.getConf().pistonRestrict){
			for (Block b : event.getBlocks()){
				if (plugin.getConf().pistonRestrictBlocks.contains(b.getType().toString())){
					event.setCancelled(true);
					break;
				}
		    }
		}
	}
	
    /*
     * PISTON  MOVE BLOCK BACKWARD, OOOF!
     */	
	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent event) {
    	if(event.isCancelled()) return;
    	// Cancel if piston moves restricted blocks BACKWARD
		if(plugin.getConf().pistonRestrict){
			for (Block b : event.getBlocks()){
				if (plugin.getConf().pistonRestrictBlocks.contains(b.getType().toString())){
					event.setCancelled(true);
					break;
				}
		    }
		}
	}
	
    /*
     * SOMETHING HAPPENS WITH REDSTONE
     */
	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event){
		if(!plugin.getConf().redStoneEnabled){
			event.setNewCurrent(0);
		}
	}
}