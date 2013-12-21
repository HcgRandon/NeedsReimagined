package com.devoverflow.reimagined.needs.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class NeedsPlayerQuitListener implements Listener {
	private Needs plugin;
		
	public NeedsPlayerQuitListener(Needs plugin) {
		this.plugin = plugin;
	}
		
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerQuitEvent event) {
		NeedsPlayer player = new NeedsPlayer(plugin, event.getPlayer());
		
		event.setQuitMessage(plugin.chatBlue + "[" + plugin.getName() + "]: " + plugin.chatDark_AQUA + player.getName() + plugin.chatGold + " left the server.");
	}
}
