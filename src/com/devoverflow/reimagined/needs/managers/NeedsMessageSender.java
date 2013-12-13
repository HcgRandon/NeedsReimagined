package com.devoverflow.reimagined.needs.managers;

import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedStrings;

public class NeedsMessageSender {
	private Needs plugin;
	
	public NeedsMessageSender(Needs plugin) {
		this.plugin = plugin;
	}
	
	public void broadcastAdmin(String message) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p.hasPermission(NeedStrings.perm_NeedsAdmin)) p.sendMessage(message);
		}
	}
	
	public void broadcastMods(String message) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p.hasPermission(NeedStrings.perm_NeedsMod)) p.sendMessage("Send to mods: " + message);
		}
		broadcastAdmin(message);
	}
	
	public void broadcastPlayer(String message) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			p.sendMessage(message);
		}
	}
}
