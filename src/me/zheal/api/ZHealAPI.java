package me.zheal.api;

import org.bukkit.Bukkit;

import me.zheal.ZHeal;

public class ZHealAPI {

	private final static ZHeal PLUGIN;
	
	static {
		PLUGIN = (ZHeal)Bukkit.getPluginManager().getPlugin("zHeal");
	}
	
	public static ZHeal getInstance() {
		return PLUGIN;
	}
	
}
