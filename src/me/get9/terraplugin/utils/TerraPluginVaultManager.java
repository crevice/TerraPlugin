package me.get9.terraplugin.utils;

import me.get9.terraplugin.TerraPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;

public class TerraPluginVaultManager {
	private TerraPlugin plugin;
	public Permission perms = null;
	public Chat chat = null;
	
	public TerraPluginVaultManager(TerraPlugin plugin){
		this.plugin = plugin;
		setupPermissions();
		setupChat();
	}
	
	// Setup Permissions
	public void setupPermissions(){
	    RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
	    perms = rsp.getProvider();
	}
	
    // Setup Chat
	public void setupChat(){
	    RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
	    chat = rsp.getProvider();
	}
	
    public Chat getChat() {
        return chat;
    }
    
	public Permission getPerms() {
		return perms;
	}
}
