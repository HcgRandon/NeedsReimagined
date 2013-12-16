package com.devoverflow.reimagined.needs.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class NeedsBackManager implements Listener {
	private Needs plugin;
	private Map<String, BackClass> deathLoc = new HashMap<String, BackClass>();
	
	public NeedsBackManager(Needs plugin) {
		this.plugin = plugin;
	}
	
	public void teleportPlayerBack(NeedsPlayer p) {
		if (!deathLoc.containsKey(p.getName())) {
			p.sendError("Back has expired or you have no recent deaths.");
			return;
		}
		//safe to do teleport.
		BackClass back = deathLoc.get(p.getName());
		p.teleportTo(back.getLocation());
		//remove player from back
		deathLoc.remove(p.getName());
		p.sendDefault("There you go.");
	}
	
	class BackClass {
		private Player player;
		private Location location;
		
		public BackClass(Player p, Location l) {
			this.player   = p;
			this.location = l;
		}
		
		public Player getPlayer() {
			return this.player;
		}
		
		public void setPlayer(Player p) {
			this.player = p;
		}
		
		public Location getLocation() {
			return location;
		}
		
		public void setLocation(Location loc) {
			this.location = loc;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Entity entity = event.getEntity();
		String name   = ((HumanEntity) entity).getName();
		
		if (plugin.getServer().getPlayer(name) != null) {
			//is online
			NeedsPlayer p = new NeedsPlayer(plugin, plugin.getServer().getPlayer(name));
			BackClass back = new BackClass(p.base, entity.getLocation());
			deathLoc.put(name, back);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		final NeedsPlayer player = new NeedsPlayer(plugin, event.getPlayer());
		if (deathLoc.containsKey(player.getName())) {
			//tell user
			player.sendDefault(plugin.chatGreen + "/back" + plugin.chatGold + " to return to the place where you fell.");
			player.sendDefault(plugin.chatGray + "1 min remaining for " + plugin.chatGreen + "/back" + plugin.chatGray + ".");
			//schedule deletion of /back
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				@Override
				public void run() {
					if (deathLoc.containsKey(player.getName())) {
						deathLoc.remove(player.getName());
						player.sendDefault(plugin.chatGreen + "/back" + plugin.chatGray + " has expired.");
					}
				}
			}, 1200L);
		}
	}
}
