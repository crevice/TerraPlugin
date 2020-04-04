package me.get9.terraplugin.listeners;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import me.get9.terraplugin.TerraPlugin;
import me.get9.terraplugin.mods.playermods.TerraPluginPlayerModsRecord;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class TerraPluginMessageListener implements PluginMessageListener {
	private TerraPlugin plugin;

	public TerraPluginMessageListener(TerraPlugin plugin){
		this.plugin = plugin;
	}
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		switch(channel){
			// Brand May Be: vanilla, liteloader, plc18, subsystem, rift, fabric, worlddownloader
			case "minecraft:brand":
		    	String brand = "UNKNOWN";
		    	try {
		    		brand = new String(data, "UTF-8");
		    	} catch (UnsupportedEncodingException e) {}
		    	plugin.getModsList().createRecord(player.getName(), new TerraPluginPlayerModsRecord(brand.trim()));
		    	plugin.log(player.getName()+" uses " + brand.trim() + " client.");
			break;
			case "fml:handshake":
		        if (data[0] == 2)
		        {
			        String mods = "";
			        boolean store = false;
			        String tempName = null;
			        for (int i = 2; i < data.length; store = !store)
			        {
			            int end = i + data[i] + 1;
			            byte[] range = Arrays.copyOfRange(data, i + 1, end);
			            String string = new String(range);
			            if (store)
			            {
			            	mods+=tempName+":"+string+", ";
			                plugin.getModsList().addModToRecord(player.getName(), tempName, string);
			            }
			            else
			            {
			                tempName = string;
			            }
			            i = end;
			        }
			    	plugin.log(player.getName()+" uses FML mods: "+mods);
		        }
			break;
			
			// Example for ToDo's
			case "wdl:init":
		    	plugin.log(player.getName()+" uses WorldDownloader");
                plugin.getModsList().addModToRecord(player.getName(), "WorldDownloader", "");
			break;
		}
	}
}
