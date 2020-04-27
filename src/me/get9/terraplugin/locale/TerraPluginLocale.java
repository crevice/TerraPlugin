package me.get9.terraplugin.locale;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import me.get9.terraplugin.TerraPlugin;
import net.md_5.bungee.api.ChatColor;

import com.google.gson.Gson;

public class TerraPluginLocale {
	
	private Map<String, String> locale = new HashMap<String,String>();
	
	// Return string from locale mapping.
	public String getString(String var, Object[] params){	
		if(locale.containsKey(var)){
			return replaceVariables(locale.get(var), params);
		}
		return getString("noLocale", new Object[]{var});
	}

	// Mostly for replacing color codes
	public String replaceVariables(String var, Object[] params){
		return String.format(ChatColor.translateAlternateColorCodes('&', var), params);
	}
	
	public static TerraPluginLocale loadLocaleFile(Path localeFile){
		// Get Default Locale File if not Exists
		if(!Files.exists(localeFile)){
			try {
	            InputStream stream = TerraPlugin.class.getResourceAsStream("/locales/locale_ru_RU.json");
	            FileUtils.copyInputStreamToFile(stream, new File(localeFile.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Gson().fromJson(readFileAsString(localeFile), TerraPluginLocale.class);
	}
	
	// Reads content from file to a String
	private static String readFileAsString(Path file){
		String content = "";
		try {
			content = new String(Files.readAllBytes(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}
