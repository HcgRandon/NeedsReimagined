package com.devoverflow.reimagined.needs.res;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;

public class NeedsWorld {
	private String worldname;
	private GameMode gamemode;
	private Boolean allAllowed = true;
	private List<String> allowedPlayers = new ArrayList<String>();;
	
	public NeedsWorld(String worldname, GameMode gamemode) {
		this.worldname = worldname;
		this.gamemode  = gamemode;
	}
	
	public String getWorldName() {
		return worldname;
	}
	
	public void setWorldName(String world) {
		this.worldname = world;
	}
	
	public GameMode getGamemode() {
		return gamemode;
	}
	
	public void setAllowedPlayers(List<String> players) {
		this.allAllowed = false;
		this.allowedPlayers = players;
	}
	
	public void appendAllowedPlayer(String player) {
		player = player.toLowerCase();
		if (player.isEmpty()) return;
		if (!this.allAllowed) this.allAllowed = false;
		if (!this.allowedPlayers.contains(player)) this.allowedPlayers.add(player);
	}
	
	public List<String> getAllowedPlayers() {
		return this.allowedPlayers;
	}
	
	public boolean playerAllowed(NeedsPlayer p) {
		if (this.allAllowed) return true;
		if (this.allowedPlayers.contains(p.getName().toLowerCase())) return true;
		return false;
	}
}
