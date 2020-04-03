package me.get9.terraplugin.mods.playermods;

import java.util.HashSet;
import java.util.Set;

public class TerraPluginPlayerModsRecord {
	private String clientBrand;
	private Set<String> mods;
	public TerraPluginPlayerModsRecord(String clientBrand){
		this.clientBrand = clientBrand;
		mods = new HashSet<String>();
	}
	
	public String getClientBrand(){
		return clientBrand;
	}
	
	public void addMod(String mod, String version){
		mods.add(mod + ":" +version);
	}
	
	public Set<String> getMods(){
		return mods;
	}
}
