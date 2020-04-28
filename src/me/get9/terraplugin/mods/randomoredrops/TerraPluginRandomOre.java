package me.get9.terraplugin.mods.randomoredrops;

import java.util.HashSet;
import java.util.Set;

public class TerraPluginRandomOre {
	public final String type;
	public final String name;
	public final boolean canBeMinedNaturally;
	public final double chance;
	public final double weight;
	public final int xp;
	public final double minHeight;
	public final double maxHeight;
	public final Set<String> biome;
	public TerraPluginRandomOre(String type, String name, boolean canBeMinedNaturally, double chance, double weight , int xp, double minHeight, double maxHeight, HashSet<String> biome){
		this.type = type;
		this.name = name;
		this.canBeMinedNaturally = false;
		this.chance = chance;
		this.weight = weight;
		this.xp = xp;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.biome = biome;
	}
}
