package com.devoverflow.reimagined.needs.res;

import org.bukkit.GameMode;

public class NeedsWorld {
	private String worldname;
	private GameMode gamemode;
	
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
}
