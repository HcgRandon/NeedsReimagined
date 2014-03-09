package com.devoverflow.reimagined.needs.managers;

import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsValues;

public class NeedsMessageSender {
	private Needs plugin;
	
	public NeedsMessageSender(Needs plugin) {
		this.plugin = plugin;
	}
	
	public void broadcastAdmin(String message) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p.hasPermission(NeedsValues.perm_NeedsAdmin)) p.sendMessage(message);
		}
		
		plugin.log.i(plugin.LOG_TAG, message);
	}
	
	public void broadcastMods(String message) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p.hasPermission(NeedsValues.perm_NeedsMod)) p.sendMessage("Send to mods: " + message);
		}
		broadcastAdmin(message);
	}
	
	public void broadcastPlayer(String message) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			p.sendMessage(message);
		}
		
		//tell console cuz its cool.
		plugin.log.i(plugin.LOG_TAG, message);
	}
}
