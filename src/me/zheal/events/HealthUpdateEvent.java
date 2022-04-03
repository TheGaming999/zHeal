package me.zheal.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HealthUpdateEvent extends Event implements Cancellable {
	
    private Player player;
    private UpdateType updateType;
    private double newHealth;
    private double oldHealth;
    private CommandSender healer;
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public HealthUpdateEvent(Player player, CommandSender healer, UpdateType updateType, double newHealth, double oldHealth) {
    	this.player = player;
    	this.updateType = updateType;
    	this.newHealth = newHealth;
    	this.oldHealth = oldHealth;
    	this.healer = healer;
    }
    
	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}
	
	public Player getHealed() {
		return this.player;
		
	}
	
	public CommandSender getHealer() {
		return this.healer;
	}
	
	public double getNewHealth() {
		return this.newHealth;
	}
	
	public void setNewHealth(double newHealth) {
		this.newHealth = newHealth;
	}
	
	public double getOldHealth() {
		return this.oldHealth;
	}
	
	public UpdateType getUpdateType() {
		return this.updateType;
	}
	
}
