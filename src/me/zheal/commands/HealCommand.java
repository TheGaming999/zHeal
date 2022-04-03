package me.zheal.commands;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zheal.ZHeal;
import me.zheal.data.Messages;
import me.zheal.data.Settings;
import me.zheal.events.HealthUpdateEvent;
import me.zheal.events.UpdateType;
import me.zheal.utils.ActionBar;
import me.zheal.utils.CooldownScheduler;
import me.zheal.utils.XSound;
import xyz.xenondevs.particle.ParticleBuilder;

@SuppressWarnings("deprecation")
public class HealCommand implements CommandExecutor {

	private ZHeal plugin;
	private Settings settings;
	private Messages messages;

	public HealCommand(ZHeal plugin) {
		this.plugin = plugin;
		this.settings = this.plugin.getSettings();
		this.messages = this.plugin.getMessages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getLabel().equalsIgnoreCase("heal")) {

			if(!sender.hasPermission(settings.permission_heal)) 
				return sendMsg(sender, messages.noPermission);

			switch (args.length) {

			case 0:

				if(!(sender instanceof Player)) return sendMsg(sender, Messages.HEAL_OTHER_SYNTAX);

				Player player = (Player)sender;

				if(!settings.isHealCooldown ||
						sender.hasPermission(settings.permission_heal_cooldown_bypass)) {
					HealthUpdateEvent e = new HealthUpdateEvent(player, player, UpdateType.FILL, player.getMaxHealth(), player.getHealth());
					Bukkit.getPluginManager().callEvent(e);
					if(e.isCancelled()) return true;
					healPlayer(player);
					sendMsg(player, messages.heal);	
					sendSound(player, settings.healSoundName, settings.healSoundVolume, settings.healSoundPitch);
					sendParticle(player, settings.healParticlesName);
					sendAB(player, messages.actionBarHeal);
					return true;
				}	

				CooldownScheduler.schedule(player.getName(), getCooldownDuration(player))
				.ifTrue(() -> {	
					HealthUpdateEvent e = new HealthUpdateEvent(player, player, UpdateType.FILL, player.getMaxHealth(), player.getHealth());
					Bukkit.getPluginManager().callEvent(e);
					if(e.isCancelled()) return;
					healPlayer(player);
					sendMsg(player, messages.heal);
					sendAB(player, messages.actionBarHeal);
					sendSound(player, settings.healSoundName, settings.healSoundVolume, 
							settings.healSoundPitch);
					sendParticle(player, settings.healParticlesName);
				})
				.orElse(timeLeft -> {
					String left = String.valueOf(timeLeft);
					sendMsg(player, messages.healUnderCooldown.replace("%left%", left));
				});

				break;
			case 1:

				switch (args[0].toLowerCase()) {

				case "all": case "*": case "@a":

					if(!sender.hasPermission(settings.permission_heal_all))
						return sendMsg(sender, messages.noPermission);

					Bukkit.getOnlinePlayers().forEach(this::healPlayer);
					plugin.doAsync(() -> {
						sendMsg(sender, messages.healAllSend);
						bcMsg(messages.healAllRecieve.replace("%player%", sender.getName()));
						bcAB(messages.actionBarHeallAllReceive);
						Bukkit.getOnlinePlayers().forEach(all -> {
							sendParticle(all);
							sendSound(all, settings.healSoundName, settings.healSoundVolume, settings.healSoundPitch);
						});
					});

					break;
				case "me": case "@s": case "@p":

					if(!sender.hasPermission(settings.permission_heal_other))
						return sendMsg(sender, messages.noPermission);

					if(!(sender instanceof Player)) return sendMsg(sender, Messages.HEAL_OTHER_SYNTAX);

					Player target = (Player)sender;
					HealthUpdateEvent e = new HealthUpdateEvent(target, target, UpdateType.FILL, target.getMaxHealth(), target.getHealth());
					Bukkit.getPluginManager().callEvent(e);
					if(e.isCancelled()) return true;
					healPlayer(target);
					sendMsg(target, messages.healOtherSend.replace("%player%", target.getName()));
					sendMsg(target, messages.healOtherReceive.replace("%player%", target.getName()));
					sendAB(target, messages.actionBarHeal);
					sendAB(target, messages.actionBarHealOtherReceive.replace("%player%",
							target.getName()));
					sendSound(target, settings.healSoundName, settings.healSoundVolume, 
							settings.healSoundPitch);
					sendParticle(target, settings.healParticlesName);

					break;
				case "random": case "@r": case "?":

					if(!sender.hasPermission(settings.permission_heal_other))
						return sendMsg(sender, messages.noPermission);

					Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
					int indexSize = onlinePlayers.length-1;
					int randomInt = indexSize <= 0 ? 0 :
						ThreadLocalRandom.current().nextInt(0, indexSize);
					target = onlinePlayers[randomInt];
					HealthUpdateEvent es = new HealthUpdateEvent(target, sender, UpdateType.FILL, target.getMaxHealth(), target.getHealth());
					Bukkit.getPluginManager().callEvent(es);
					if(es.isCancelled()) return true;
					healPlayer(target);
					sendMsg(sender, messages.healOtherSend.replace("%player%", target.getName()));
					sendMsg(target, messages.healOtherReceive.replace("%player%", sender.getName()));
					sendAB(target, messages.actionBarHealOtherReceive.replace("%player%",
							sender.getName()));
					sendSound(target, settings.healSoundName, settings.healSoundVolume, 
							settings.healSoundPitch);
					sendParticle(target, settings.healParticlesName);

					break;
				default:

					if(!sender.hasPermission(settings.permission_heal_other))
						return sendMsg(sender, messages.noPermission);

					target = Bukkit.getPlayer(args[0]);
					if(target == null)
						return sendMsg(sender, messages.healOtherUnknownPlayer
								.replace("%player%", args[0]));

					HealthUpdateEvent ea = new HealthUpdateEvent(target, sender, UpdateType.FILL, target.getMaxHealth(), target.getHealth());
					Bukkit.getPluginManager().callEvent(ea);
					if(ea.isCancelled()) return true;
					healPlayer(target);
					sendMsg(sender, messages.healOtherSend.replace("%player%", target.getName()));
					sendMsg(target, messages.healOtherReceive.replace("%player%", sender.getName()));
					sendAB(target, messages.actionBarHealOtherReceive.replace("%player%",
							sender.getName()));
					sendSound(target, settings.healSoundName, settings.healSoundVolume, 
							settings.healSoundPitch);
					sendParticle(target, settings.healParticlesName);

					break;
				}
				break;
			default:
				break;
			}
		}
		return true;
	}

	public void healPlayer(Player player) {
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setExhaustion(0);
		player.setSaturation(10);
	}

	public boolean sendMsg(CommandSender sender, String message) {
		if(message == null || message.isEmpty())
			return true;
		sender.sendMessage(message);
		return true;
	}

	public boolean bcMsg(String message) {
		if(message == null || message.isEmpty())
			return true;
		Bukkit.broadcastMessage(message);
		return true;
	}

	public boolean sendAB(Player player, String message) {
		if(!settings.isSendActionBarMessages || message == null || message.isEmpty())
			return true;
		ActionBar.sendActionBar(player, message);
		return true;
	}

	public boolean bcAB(String message) {
		if(!settings.isSendActionBarMessages || message == null || message.isEmpty())
			return true;
		plugin.doAsync(() -> {
			ActionBar.sendPlayersActionBar(message);
		});
		return true;
	}

	public boolean sendSound(Player player, String sound, float volume, float pitch) {
		if(!settings.isHealSound || sound == null || sound.isEmpty())
			return true;
		player.playSound(player.getLocation(), XSound.matchXSound(sound).get().parseSound(), volume, pitch);
		return true;
	}

	public boolean sendParticle(Player player, List<ParticleBuilder> particle) {
		if(!settings.isHealParticles || particle == null)
			return true;
		plugin.doAsync(() -> {
			particle.forEach(pb -> {
				Location loc = pb.getLocation();
				loc.setWorld(player.getWorld());
				pb.setLocation(player.getLocation().add(loc)).display();
				pb.setLocation(loc);
			});
		});
		return true;
	}

	public boolean sendParticle(Player player) {
		List<ParticleBuilder> particle = settings.healParticlesName;
		if(!settings.isHealParticles || particle == null)
			return true;
		plugin.doAsync(() -> {
			particle.forEach(pb -> {
				Location loc = pb.getLocation();
				loc.setWorld(player.getWorld());
				pb.setLocation(player.getLocation().add(loc)).display();
				pb.setLocation(loc);
			});
		});
		return true;
	}

	public int getCooldownDuration(Player player) {
		int cooldown = settings.healCooldownDefaultDuration;
		for(Entry<String, Integer> entry : settings.healCooldownDurationsMap.entrySet()) {
			String permission = entry.getKey();
			if(player.hasPermission(permission)) cooldown = entry.getValue();
		}
		return cooldown;
	}

}
