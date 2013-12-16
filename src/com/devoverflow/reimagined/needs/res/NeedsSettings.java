package com.devoverflow.reimagined.needs.res;

import java.io.File;
import com.devoverflow.reimagined.needs.Needs;

public class NeedsSettings {
	private Needs plugin;
	private String LOG_TAG = Needs.STATIC_LOG_TAG;
	
	public final String SECTION_WORLDS = "worlds";
	
	private File configdir, mainconfig, playerdir;
	
	public NeedsSettings(Needs plugin) {
		this.plugin  = plugin;
		
		configdir    = this.plugin.getDataFolder();
		playerdir    = new File(this.plugin.getDataFolder() + File.separator + "players");
		mainconfig   = new File(this.plugin.getDataFolder() + File.separator + "main.json");
		
		if (!configdir.exists()) configdir.mkdir();
		if (!playerdir.exists()) playerdir.mkdir();
		
		JSONFileParser config = new JSONFileParser(mainconfig);
		if (config.error() != null) {
			plugin.log.e(LOG_TAG, "Failed to load main.json: " + config.error().getError());
		}
	}
}
