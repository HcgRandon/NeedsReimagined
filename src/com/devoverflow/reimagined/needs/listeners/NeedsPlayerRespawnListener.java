package com.devoverflow.reimagined.needs.listeners;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class NeedsPlayerRespawnListener implements Listener{
	private Needs plugin;
	
	public NeedsPlayerRespawnListener(Needs plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		NeedsPlayer player = new NeedsPlayer(plugin, event.getPlayer());
		
		Location location = event.getRespawnLocation();
		File yamlFile = new File(plugin.getPlayerDir(player.getPlayer()) + File.separator + "homes.yml");
		FileConfiguration hConf = YamlConfiguration.loadConfiguration(yamlFile);
		
		String start = player.getWorld().getName();
		
		Object x     = hConf.get(start + ".x", null);
		Object y     = hConf.get(start + ".y", null);
		Object z     = hConf.get(start + ".z", null);
		Object yaw   = hConf.get(start + ".yaw", null);
		Object pitch = hConf.get(start + ".pitch", null);
		
		if (x == null && y == null && z == null && yaw == null && pitch == null) {
			player.sendDefault("No home set, spawn it is then.");
		}else{
			location = new Location(player.getWorld(), Double.parseDouble(x.toString()), Double.parseDouble(y.toString()), Double.parseDouble(z.toString()), Float.parseFloat(yaw.toString()), Float.parseFloat(pitch.toString()));
			player.sendDefault("Taking you to your home.");
		}
		
		event.setRespawnLocation(location);
	}
}
