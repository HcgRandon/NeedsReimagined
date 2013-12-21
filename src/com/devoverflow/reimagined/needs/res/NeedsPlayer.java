package com.devoverflow.reimagined.needs.res;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;

public class NeedsPlayer{
	private Needs plugin;
	private String LOG_TAG;
	private Player base;
	
	public NeedsPlayer(Needs plugin, Player p) {
		this.plugin  = plugin;
		this.LOG_TAG = plugin.LOG_TAG;
		this.base    = p;
	}
	
	public Player getPlayer() {
		return this.base;
	}
	
	public World getWorld() {
		return this.base.getWorld();
	}
	
	public Location getLocation() {
		return this.base.getLocation();
	}
	
	public Boolean hasAgroMob() {
		//check any agro on our player
		Boolean isAgro = false;
		for (Entity entity : this.base.getNearbyEntities(20, 20, 20)) {
			if (!(entity instanceof Monster)) continue;
			LivingEntity target = ((Monster)entity).getTarget();
			if (!(target instanceof Player)) continue;
			Player p = (Player)target;
			if (p == this.base) {
				isAgro = true;
				break;
			}
		}
		return isAgro;
	}
	
	public void sendDefault(String message) {
		base.sendMessage(plugin.chatBlue + "[" + LOG_TAG + "]: " + plugin.chatGold + message + plugin.chatWhite);
	}
	
	public void sendError(String error) {
		base.sendMessage(plugin.chatBlue + "[" + LOG_TAG + "]: " + plugin.chatRed + error + plugin.chatWhite);
	}
	
	public void sendSuccess(String yay) {
		base.sendMessage(plugin.chatBlue + "[" + LOG_TAG + "]: " + plugin.chatGreen + yay + plugin.chatWhite);
	}
	
	public void sendRaw(String message) {
		base.sendMessage(message);
	}
	
	public boolean hasPermission(String perm) {
		return base.hasPermission(perm);
	}
	
	public void teleportTo(Location l) {
		//if worlds differ change to other world first
		if (l.getWorld() != base.getWorld()) plugin.nwm.teleportPlayer(this, l.getWorld());
		
		//then to teleport
		base.teleport(l);
	}
	
	public String getName() {
		return base.getName();
	}
}
