package me.zheal;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import me.zheal.commands.GiveHealthCommand;
import me.zheal.commands.HealCommand;
import me.zheal.commands.RemoveHealthCommand;
import me.zheal.commands.SetHealthCommand;
import me.zheal.commands.SetMaxHealthCommand;
import me.zheal.commands.ZHealCommand;
import me.zheal.data.Messages;
import me.zheal.data.Settings;
import me.zheal.utils.ConfigCreator;

public class ZHeal extends JavaPlugin {

	private FileConfiguration config;
	private FileConfiguration messagesConfig;
	private Settings settings;
	private Messages messages;
	private HealCommand healCommand;
	private ZHealCommand zHealCommand;
	private GiveHealthCommand giveHealthCommand;
	private SetHealthCommand setHealthCommand;
	private RemoveHealthCommand removeHealthCommand;
	private SetMaxHealthCommand setMaxHealthCommand;
	private BukkitScheduler scheduler;

	@Override
	public void onEnable() {
		this.scheduler = Bukkit.getScheduler();
		this.config = ConfigCreator.copyAndSaveDefaults("config.yml");
		this.messagesConfig = ConfigCreator.copyAndSaveDefaults("messages.yml");
		this.settings = new Settings(this);
		this.messages = new Messages(this);
		this.healCommand = new HealCommand(this);
		this.zHealCommand = new ZHealCommand(this);
		this.giveHealthCommand = new GiveHealthCommand(this);
		this.setHealthCommand = new SetHealthCommand(this);
		this.removeHealthCommand = new RemoveHealthCommand(this);
		this.setMaxHealthCommand = new SetMaxHealthCommand(this);
		this.getCommand("heal").setExecutor(healCommand);
		this.getCommand("zheal").setExecutor(zHealCommand);
		this.getCommand("givehealth").setExecutor(giveHealthCommand);
		this.getCommand("sethealth").setExecutor(setHealthCommand);
		this.getCommand("removehealth").setExecutor(removeHealthCommand);
		this.getCommand("setmaxhealth").setExecutor(setMaxHealthCommand);
		this.getLogger().info("Enabled");
	}

	@Override
	public void onDisable() {
		this.getLogger().info("Disabled");
	}

	public CompletableFuture<Void> reload() {
		return CompletableFuture.runAsync(() -> {
			this.config = ConfigCreator.reloadConfig("config.yml");
			this.messagesConfig = ConfigCreator.reloadConfig("messages.yml");
			this.settings = new Settings(this);
			this.messages = new Messages(this);
			this.healCommand = new HealCommand(this);
			this.getCommand("heal").setExecutor(healCommand);
		}).exceptionally(ex -> {
			ex.printStackTrace();
			return null;
		});
	}
	
	public CompletableFuture<Void> reload(CommandSender sender) {
		return CompletableFuture.runAsync(() -> {
			this.config = ConfigCreator.reloadConfig("config.yml");
			this.messagesConfig = ConfigCreator.reloadConfig("messages.yml");
			this.settings = new Settings(this);
			this.messages = new Messages(this);
			this.healCommand = new HealCommand(this);
			this.getCommand("heal").setExecutor(healCommand);
		}).exceptionally(ex -> {
			ex.printStackTrace();
			sender.sendMessage("RELOAD FAILED!");
			return null;
		});
	}

	public BukkitTask doAsync(Runnable runnable) {
		return scheduler.runTaskAsynchronously(this, runnable);
	}

	public Messages getMessages() {
		return this.messages;
	}

	public Settings getSettings() {
		return this.settings;
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public FileConfiguration getMessagesConfig() {
		return this.messagesConfig;
	}

	public HealCommand getHealCommand() {
		return this.healCommand;
	}
	
}
