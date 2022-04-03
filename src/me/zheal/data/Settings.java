package me.zheal.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zheal.ZHeal;
import me.zheal.utils.ParticleReader;
import xyz.xenondevs.particle.ParticleBuilder;

public class Settings {

	private ZHeal plugin;
	
	public boolean isHealParticles, isHealSound, isSendActionBarMessages,
	isHealCooldown;
	
	public String healKillerSoundName, healSoundName, permission_heal, permission_heal_other
	, permission_heal_all, permission_givehealth, permission_sethealth, permission_removehealth,
	permission_setmaxhealth, permission_reload, permission_help, permission_heal_cooldown_bypass;
	
	public float healSoundVolume, healSoundPitch;
	
	public List<ParticleBuilder> healParticlesName;
	
	public int healCooldownDefaultDuration;
	
	public Map<String, Integer> healCooldownDurationsMap = new HashMap<>();
	
	public Settings(ZHeal plugin) {
		this.plugin = plugin;
		this.load();
	}
	
	public void load() {
		// booleans
		this.isHealParticles = this.plugin.getConfig().getBoolean("Options.heal-particles");
		this.isHealSound = this.plugin.getConfig().getBoolean("Options.heal-sound");
		this.isSendActionBarMessages = this.plugin.getConfig().getBoolean("Options.send-action-bar-messages");
		this.isHealCooldown = this.plugin.getConfig().getBoolean("Options.heal-cooldown");
		// strings
		this.healKillerSoundName = this.plugin.getConfig().getString("Options.heal-killer-sound-name");
		this.healParticlesName = ParticleReader.parseAll(this.plugin.getConfig().getString("Options.heal-particles-name"));
		this.healSoundName = this.plugin.getConfig().getString("Options.heal-sound-name");
		this.permission_heal = this.plugin.getConfig().getString("Permissions.heal");
		this.permission_heal_other = this.plugin.getConfig().getString("Permissions.heal-other");
		this.permission_heal_all = this.plugin.getConfig().getString("Permissions.heal-all");
		this.permission_givehealth = this.plugin.getConfig().getString("Permissions.givehealth");
		this.permission_sethealth = this.plugin.getConfig().getString("Permissions.sethealth");
		this.permission_removehealth = this.plugin.getConfig().getString("Permissions.removehealth");
		this.permission_setmaxhealth = this.plugin.getConfig().getString("Permissions.setmaxhealth");
		this.permission_reload = this.plugin.getConfig().getString("Permissions.reload");
		this.permission_help = this.plugin.getConfig().getString("Permissions.help");
		this.permission_heal_cooldown_bypass = this.plugin.getConfig().getString("Permissions.heal-cooldown-bypass");
		// floats
		this.healSoundVolume = (float)this.plugin.getConfig().getDouble("Options.heal-sound-volume");
		this.healSoundPitch = (float)this.plugin.getConfig().getDouble("Options.heal-sound-pitch");
		// durations setup
		this.healCooldownDefaultDuration = this.plugin.getConfig().getInt("Options.heal-cooldown-default-duration");
		this.plugin.getConfig().getStringList("Options.heal-cooldown-durations").forEach(stringLine -> {
			String[] split = stringLine.split("->");
			// 1: perm - 2: duration
			this.healCooldownDurationsMap.put(split[0], Integer.parseInt(split[1]));
		});
	}
	
}
