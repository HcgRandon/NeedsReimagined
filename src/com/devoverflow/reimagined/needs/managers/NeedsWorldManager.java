package com.devoverflow.reimagined.needs.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.JSONFileParser;
import com.devoverflow.reimagined.needs.res.JSONHelper;
import com.devoverflow.reimagined.needs.res.NeedsLogger;
import com.devoverflow.reimagined.needs.res.json.JSONObject;

@SuppressWarnings("unused")
public class NeedsWorldManager {
	private Needs plugin;
	private String LOG_TAG;
	private JSONFileParser nfp;
	private NeedsLogger Log;
	private List<String> loadedworlds = new ArrayList<String>();
	
	private File worldsfile;
	private String WORLDS_WRAPPER   = "worlds";
	private String SECTION_DEFAULTS = "defaults";
	private String SECTION_GAMERULE = "gamerules";
	private String SECTION_PLAYERS  = "players";
	
	public NeedsWorldManager(Needs plugin) {
		this.plugin     = plugin;
		this.worldsfile = new File(plugin.getDataFolder() + File.separator + "worlds.json");
		this.Log        = new NeedsLogger();
		this.nfp        = new JSONFileParser(this.worldsfile);
		this.LOG_TAG    = plugin.LOG_TAG + "->WorldMan";
		
		loadWorlds();
	}
	
	@SuppressWarnings("unchecked")
	private void loadWorlds() {
		//pull the jsonObject
		JSONHelper worlds = new JSONHelper(); 
		if (nfp.json.get(WORLDS_WRAPPER, null) == null) {
			Log.i(LOG_TAG, "Need to save");
			nfp.json.set(WORLDS_WRAPPER, new JSONObject());
			nfp.save();
		}
		worlds.setJSONObject((JSONObject)nfp.json.get(WORLDS_WRAPPER, new JSONObject()));
		
		Iterator<String> worldi = (Iterator<String>) worlds.getJSONObject().keys();
		while (worldi.hasNext()) {
			//pull the next world up
			String worldname = worldi.next();
			
			//get world config
			JSONHelper world = new JSONHelper();
			world.setJSONObject((JSONObject) worlds.get(worldname, new JSONObject()));
			
			Log.i(LOG_TAG, "Setting up world \"" + worldname + "\"");
			plugin.getServer().createWorld(new WorldCreator(worldname)); //setup world
			
			World nworld = plugin.getServer().getWorld(worldname);//get world instance
			
			//get gamerule section
			JSONHelper gameRules = new JSONHelper();
			gameRules.setJSONObject((JSONObject) world.get(SECTION_GAMERULE, new JSONObject()));
			Iterator<String> gamerulei = (Iterator<String>) gameRules.getJSONObject().keys();
			while (gamerulei.hasNext()) {
				String gamerule      = gamerulei.next();
				String gamerulevalue = gameRules.get(gamerule, "").toString();
 				Log.i(LOG_TAG + "->GameRule", gamerule + ": " + gamerulevalue);
				nworld.setGameRuleValue(gamerule, gamerulevalue);
			}
			
			if (worldname != null) this.loadedworlds.add(worldname.toString());
			
			Log.i(LOG_TAG, "Done");
		}
		
		nfp.save();
	}
	
	public List<String> getWorlds() {
		return this.loadedworlds;
	}
	
	private void teleportPlayer(Player p, World toWorld) {
		
	}
	
	private void savePlayerLoc(Player user) {
		File playerFolder = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + user.getName());
		if (!playerFolder.exists()) playerFolder.mkdirs();
		JSONFileParser jfp = new JSONFileParser(new File(playerFolder + File.separator + "inventory.json"));
		
		
	}
}
