package me.zheal.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zheal.ZHeal;
import me.zheal.data.Messages;
import me.zheal.data.Settings;

public class SetMaxHealthCommand implements CommandExecutor {

	private ZHeal plugin;

	public SetMaxHealthCommand(ZHeal plugin) {
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
		if(cmd.getLabel().equalsIgnoreCase("setmaxhealth")) {
			if(!sender.hasPermission(settings().permission_setmaxhealth)) {
				sender.sendMessage(messages().noPermission);
				return true;
			}
			if(args.length == 0 || args.length == 1) {
				sender.sendMessage(Messages.SET_MAX_HEALTH_SYNTAX);
			} else if (args.length == 2) {
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					sender.sendMessage(messages().healOtherUnknownPlayer);
					return true;
				}
				double amount = 0;
				if(args[1].startsWith("+")) {
					try {
						amount = Double.parseDouble(args[1].substring(1));
						double targetHealth = target.getMaxHealth();
						double amountToGive = targetHealth + amount;
						target.setMaxHealth(amountToGive);
					} catch (NumberFormatException ex) {
						sender.sendMessage("Invalid number");
					}
				} else if (args[1].startsWith("-")) {
					try {
						amount = Double.parseDouble(args[1].substring(1));
						double targetHealth = target.getMaxHealth();
						double amountToRemove = targetHealth - amount;
						if(amountToRemove > target.getMaxHealth()) amountToRemove = 
								target.getMaxHealth() - (amountToRemove);
						if(amountToRemove < 1) {
							amountToRemove = 1; 
							amount = 1;
						}
						target.setMaxHealth(amountToRemove);
					} catch (NumberFormatException ex) {
						sender.sendMessage("Invalid number");
					}
				} else {
					try {
						amount = Double.parseDouble(args[1]);
					} catch (NumberFormatException ex) {
						sender.sendMessage("Invalid number");
					}
					if(amount < 1) amount = 1;
					target.setMaxHealth(amount);
				}
				sender.sendMessage(messages().setMaxHealth
						.replace("%player%", target.getName())
						.replace("%amount%", String.valueOf(target.getMaxHealth()))
						.replace("%heart%", "❤"));
			}
		}
		return true;
	}

}
