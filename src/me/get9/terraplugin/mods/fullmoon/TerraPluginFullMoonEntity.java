package me.get9.terraplugin.mods.fullmoon;

import java.util.List;

import org.bukkit.potion.PotionEffect;

public class TerraPluginFullMoonEntity {
	public final String type;
	public List<PotionEffect> potioneffects;
	
	public TerraPluginFullMoonEntity(String type, List<PotionEffect> potioneffects){
		this.type = type;
		this.potioneffects = potioneffects;
	}
}
