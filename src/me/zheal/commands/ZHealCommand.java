package me.zheal.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zheal.ZHeal;
import me.zheal.utils.Colorizer;
import me.zheal.utils.ParticleReader;
import me.zheal.utils.XSound;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class ZHealCommand implements CommandExecutor {

	private ZHeal plugin;
	private final List<String> effects = ParticleEffect.getAvailableEffects().stream()
			.map(s -> Colorizer.colorize("&6" + s.name().toLowerCase()))
			.collect(Collectors.toList());
	private final List<String> sounds = Arrays.stream(XSound.VALUES)
			.filter(s -> s.isSupported())
			.map(s -> Colorizer.colorize("&c" + s.parseSound().name().toLowerCase()))
			.collect(Collectors.toList());
	private final String separator = Colorizer.colorize("&7, "); 
	private final String effectsString = String.join(separator, effects);
	private final String soundsString = String.join(separator, sounds);
	
	public ZHealCommand(ZHeal plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getLabel().equalsIgnoreCase("zheal")) {
			if(args.length == 0) {
				if(!sender.hasPermission(plugin.getSettings().permission_help)) {
					sender.sendMessage(plugin.getMessages().noPermission);
					return true;
				}
				plugin.getMessages().help.forEach(sender::sendMessage);
			} else if (args.length == 1) {
				if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					plugin.getMessages().help.forEach(sender::sendMessage);
				} else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					if(!sender.hasPermission(plugin.getSettings().permission_reload)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					plugin.reload(sender)
					.thenRunAsync(() -> sender.sendMessage(plugin.getMessages().reload));
				} else if (args[0].equalsIgnoreCase("effects")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					sender.sendMessage(effectsString);
				} else if (args[0].equalsIgnoreCase("sounds")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					sender.sendMessage(soundsString);
				}
			} else if (args.length == 2) {
				if(args[0].equalsIgnoreCase("playeffect")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					if(!(sender instanceof Player)) {
						return true;
					}
					Player player = (Player)sender;
					ParticleBuilder part = ParticleReader.parse(getArguments(args, 1));
					Location loc = part.getLocation();
					loc.setWorld(player.getWorld());
					part.setLocation(player.getLocation().add(loc)).display();
				} else if (args[0].equalsIgnoreCase("playsound")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					if(!(sender instanceof Player)) {
						return true;
					}
					Player player = (Player)sender;
					player.playSound(player.getLocation(), XSound.matchXSound(args[1]).get().parseSound(), 1, 1);
				}
			} else if (args.length == 3) {
				if(args[0].equalsIgnoreCase("playsound")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					if(!(sender instanceof Player)) {
						return true;
					}
					Player player = (Player)sender;
					player.playSound(player.getLocation(), XSound.matchXSound(args[1]).get().parseSound(), Float.valueOf(args[2]), 1);
				} else if(args[0].equalsIgnoreCase("playeffect")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					if(!(sender instanceof Player)) {
						return true;
					}
					Player player = (Player)sender;
					ParticleBuilder part = ParticleReader.parse(getArguments(args, 1));
					Location loc = part.getLocation();
					loc.setWorld(player.getWorld());
					part.setLocation(player.getLocation().add(loc)).display();
				}
			} else if (args.length >= 4) {
				if(args[0].equalsIgnoreCase("playsound")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					if(!(sender instanceof Player)) {
						return true;
					}
					Player player = (Player)sender;
					player.playSound(player.getLocation(), XSound.matchXSound(args[1]).get().parseSound(), Float.valueOf(args[2]), Float.valueOf(args[3]));
				} else if(args[0].equalsIgnoreCase("playeffect")) {
					if(!sender.hasPermission(plugin.getSettings().permission_help)) {
						sender.sendMessage(plugin.getMessages().noPermission);
						return true;
					}
					if(!(sender instanceof Player)) {
						return true;
					}
					Player player = (Player)sender;
					ParticleBuilder part = ParticleReader.parse(getArguments(args, 1));
					Location loc = part.getLocation();
					loc.setWorld(player.getWorld());
					part.setLocation(player.getLocation().add(loc)).display();
				}
			}
		}
		return true;
	}
	
	public String getArguments(String[] args, int num) { 
		StringBuilder sb = new StringBuilder();
		for(int i = num; i < args.length; i++) {
			sb.append(args[i]).append(" ");
		}
		return sb.toString().trim();
	}

}
