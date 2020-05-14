package me.get9.terraplugin.mods.fullmoon;

import java.util.Map;

public class TerraPluginFullMoonEntity {
	public final String type;
	public Map<String, Integer[]> potioneffects;
	
	public TerraPluginFullMoonEntity(String type, Map<String, Integer[]> potioneffects){
		this.type = type;
		this.potioneffects = potioneffects;
	}
}
