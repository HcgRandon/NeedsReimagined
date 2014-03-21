package com.devoverflow.reimagined.needs.res;

import java.io.File;
import com.devoverflow.reimagined.needs.Needs;

public class NeedsSettings {
	private Needs plugin;
	private File configdir, mainconfig, playerdir;
	
	public NeedsSettings(Needs plugin) {
		this.plugin  = plugin;
		
		configdir    = this.plugin.getDataFolder();
		playerdir    = new File(this.plugin.getDataFolder() + File.separator + "players");
		mainconfig   = new File(this.plugin.getDataFolder() + File.separator + "config.yml");
		
		if (!configdir.exists()) configdir.mkdir();
		if (!playerdir.exists()) playerdir.mkdir();
	}
}
