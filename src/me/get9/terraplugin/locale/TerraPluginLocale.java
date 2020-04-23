package me.get9.terraplugin.locale;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import me.get9.terraplugin.TerraPlugin;
import net.md_5.bungee.api.ChatColor;

import com.google.gson.Gson;

public class TerraPluginLocale {
	private Path localeFile;
	
	private Map<String, String> locale = new HashMap<String,String>();
	
	public TerraPluginLocale(Path dataFolder, String fileName){
    	this.localeFile = Paths.get(dataFolder + File.separator + fileName);
    	
		if(!Files.exists(dataFolder)){
			try {
				Files.createDirectory(dataFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!Files.exists(localeFile)){
			try {
	            InputStream stream = TerraPlugin.class.getResourceAsStream("/locale_ru_RU.json");
	            FileUtils.copyInputStreamToFile(stream, new File(localeFile.toString()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getString(String var, Object[] params){	
		if(locale.containsKey(var)){
			return replaceVariables(locale.get(var), params);
		}
		return getString("noLocale", new Object[]{var});
	}

	public String replaceVariables(String var, Object[] params){
		return String.format(ChatColor.translateAlternateColorCodes('&', var), params);
	}
	
	public static TerraPluginLocale loadJsonConfig(Path dataFolder, String fileName){
		String json = "";
		TerraPluginLocale temp = new TerraPluginLocale(dataFolder, fileName);
		try {
			json = new String(Files.readAllBytes(temp.localeFile));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			temp = new Gson().fromJson(json, TerraPluginLocale.class);
			temp.localeFile = Paths.get(dataFolder + File.separator + fileName);
		}
		return temp;
	}
}
