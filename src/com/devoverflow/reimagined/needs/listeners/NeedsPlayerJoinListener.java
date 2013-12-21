package com.devoverflow.reimagined.needs.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class NeedsPlayerJoinListener implements Listener{
	private Needs plugin;
	
	public NeedsPlayerJoinListener(Needs plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		NeedsPlayer player = new NeedsPlayer(plugin, event.getPlayer());
		
		//player.sendDefault("A update: ");
		player.sendDefault("Needs version: " + plugin.chatItalic + plugin.getNeedsVersion());
		
		event.setJoinMessage(plugin.chatBlue + "[" + plugin.getName() + "]: " + plugin.chatDark_AQUA + player.getName() + plugin.chatGold + " joined the server.");
	}
}
