package me.zheal.data;

import java.util.List;

import me.zheal.ZHeal;
import me.zheal.utils.Colorizer;

public class Messages {

	private ZHeal plugin;
	
	/**
	 * Available Placeholders:
	 * <p>healOtherSend = %player%
	 * <p>healOtherReceive = %player%
	 * <p>healOtherUnknownPlayer = %player%
	 * <p>healAllReceive = %player%
	 * <p>healUnderCooldown = %left%
	 * <p>giveHealth = %amount% %heart% %player%
	 * <p>setHealth = %amount% %heart% %player%
	 * <p>removeHealth = %amount% %heart% %player%
	 * <p>setMaxHealth = %amount% %heart% %player%
	 */ 
	public String reload, heal, healOtherSend, healOtherReceive, healOtherUnknownPlayer, healAllSend,
	healAllRecieve, healUnderCooldown, giveHealth, setHealth, removeHealth, setMaxHealth, noPermission;
	
	public List<String> help;
	
	/**
	 * Available Placeholders:
	 * <p>actionBarHealOtherReceive = %player%
	 */
	public String actionBarHeal, actionBarHealOtherReceive, actionBarHeallAllReceive;
	
	public final static String HEAL_OTHER_SYNTAX = Colorizer.colorize("&7Usage: &a/heal &6<target/all/random>");
	public final static String GIVE_HEALTH_SYNTAX = Colorizer.colorize("&7Usage: &a/givehealth &6<target> <amount>");
	public final static String SET_HEALTH_SYNTAX = Colorizer.colorize("&7Usage: &a/sethealth &6<target> <amount>");
	public final static String REMOVE_HEALTH_SYNTAX = Colorizer.colorize("&7Usage: &a/removehealth &6<target> <amount>");
	public final static String SET_MAX_HEALTH_SYNTAX = Colorizer.colorize("&7Usage: &a/setmaxhealth &6<target> <amount>"
			+ "\n&a/setmaxhealth &6<target> +<plusamount>\n&a/setmaxhealth &6<target> -<minusamount>");
	
	public Messages(ZHeal plugin) {
		this.plugin = plugin;
		this.load();
	}
	
	private String get(String configNode) {
		return Colorizer.colorize(this.plugin
				.getMessagesConfig()
				.getString("Chat-Messages." + configNode));
	}
	
	private String getActionBar(String configNode) {
		return Colorizer.colorize(this.plugin
				.getMessagesConfig()
				.getString("Action-Bar-Messages." + configNode));
	}
	
	public void load() {
		this.reload = this.get("reload");
		this.heal = this.get("heal");
		this.healOtherSend = this.get("heal-other-send");
		this.healOtherReceive = this.get("heal-other-receive");
		this.healOtherUnknownPlayer = this.get("heal-other-unknown-player");
		this.healAllSend = this.get("heal-all-send");
		this.healAllRecieve = this.get("heal-all-receive");
		this.healUnderCooldown = this.get("heal-under-cooldown");
		this.giveHealth = this.get("givehealth");
		this.setHealth = this.get("sethealth");
		this.removeHealth = this.get("removehealth");
		this.setMaxHealth = this.get("setmaxhealth");
		this.noPermission = this.get("no-permission");
		this.help = Colorizer.colorize(plugin.getMessagesConfig().getStringList("Chat-Messages.help"));
		
		this.actionBarHeal = this.getActionBar("heal");
		this.actionBarHealOtherReceive = this.getActionBar("heal-other-receive");
		this.actionBarHeallAllReceive = this.getActionBar("heal-all-receive");
	}
	
}
