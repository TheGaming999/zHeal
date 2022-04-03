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

public class SetHealthCommand implements CommandExecutor {

	private ZHeal plugin;
	
	public SetHealthCommand(ZHeal plugin) {
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
		if(cmd.getLabel().equalsIgnoreCase("sethealth")) {
			if(!sender.hasPermission(settings().permission_sethealth)) {
				sender.sendMessage(messages().noPermission);
				return true;
			}
			if(args.length == 0 || args.length == 1) {
				sender.sendMessage(Messages.SET_HEALTH_SYNTAX);
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
				if(amount > target.getMaxHealth()) amount = target.getMaxHealth();
				HealthUpdateEvent e = new HealthUpdateEvent(target, sender, UpdateType.SET, amount, target.getHealth());
				Bukkit.getPluginManager().callEvent(e);
				if(e.isCancelled()) return true;
				target.setHealth(e.getNewHealth());
				sender.sendMessage(messages().setHealth
						.replace("%player%", target.getName())
						.replace("%amount%", String.valueOf(amount))
						.replace("%heart%", "❤"));
			}
		}
		return true;
	}

}
