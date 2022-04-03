package me.zheal.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zheal.ZHeal;
import me.zheal.data.Messages;
import me.zheal.data.Settings;
import me.zheal.events.HealthUpdateEvent;
import me.zheal.events.UpdateType;

public class GiveHealthCommand implements CommandExecutor {

	private ZHeal plugin;
	
	public GiveHealthCommand(ZHeal plugin) {
		this.plugin = plugin;
	}
	
	public Settings settings() {
		return plugin.getSettings();
	}
	
	public Messages messages() {
		return plugin.getMessages();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getLabel().equalsIgnoreCase("givehealth")) {
			if(!sender.hasPermission(settings().permission_givehealth)) {
				sender.sendMessage(messages().noPermission);
				return true;
			}
			if(args.length == 0 || args.length == 1) {
				sender.sendMessage(Messages.GIVE_HEALTH_SYNTAX);
			} else if (args.length == 2) {
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					sender.sendMessage(messages().healOtherUnknownPlayer);
					return true;
				}
				double amount = 0;
				try {
					amount = Double.parseDouble(args[1]);
				} catch (NumberFormatException ex) {
					sender.sendMessage("Invalid number");
				}
				double targetHealth = target.getHealth();
				double amountToGive = targetHealth + amount;
				if(amountToGive > target.getMaxHealth()) amountToGive = 
						target.getMaxHealth() - (amountToGive);
				if(amountToGive < 0) amountToGive = target.getMaxHealth();
				HealthUpdateEvent e = new HealthUpdateEvent(target, sender, UpdateType.ADDITION, amountToGive, target.getHealth());
				Bukkit.getPluginManager().callEvent(e);
				if(e.isCancelled()) return true;
				target.setHealth(e.getNewHealth());
				sender.sendMessage(messages().giveHealth
						.replace("%player%", target.getName())
						.replace("%amount%", String.valueOf(amount))
						.replace("%heart%", "❤"));
			}
		}
		return true;
	}

}
