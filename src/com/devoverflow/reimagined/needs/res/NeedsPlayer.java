package com.devoverflow.reimagined.needs.res;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;

public class NeedsPlayer{
	private Needs plugin;
	private String LOG_TAG;
	public Player base;
	
	public NeedsPlayer(Needs plugin, Player p) {
		this.plugin  = plugin;
		this.LOG_TAG = plugin.LOG_TAG;
		this.base    = p;
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
		base.teleport(l);
	}
	
	public String getName() {
		return base.getName();
	}
}
