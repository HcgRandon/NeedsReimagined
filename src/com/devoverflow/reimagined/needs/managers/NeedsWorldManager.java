package com.devoverflow.reimagined.needs.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsLogger;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;
import com.devoverflow.reimagined.needs.res.NeedsWorld;

@SuppressWarnings("unused")
public class NeedsWorldManager {
	private Needs plugin;
	private String LOG_TAG;
	private NeedsLogger Log;
	private List<NeedsWorld> loadedworlds = new ArrayList<NeedsWorld>();
	
	private File worldsfile;
	private String WORLDS_WRAPPER   = "worlds";
	private String SECTION_DEFAULTS = "defaults";
	private String SECTION_GAMERULE = "gamerules";
	private String SECTION_PLAYERS  = "players";
	
	public NeedsWorldManager(Needs plugin) {
		this.plugin     = plugin;
		this.worldsfile = new File(plugin.getDataFolder() + File.separator + "worlds.yml");
		this.Log        = new NeedsLogger();
		this.LOG_TAG    = plugin.LOG_TAG + "->WorldMan";
		
		if (!this.worldsfile.exists()) {
			try {
				this.worldsfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		loadWorlds2();
	}
	
	private void updateGamemode(NeedsPlayer p, World w) {
		String wName = w.getName();
		for (NeedsWorld world : this.loadedworlds) {
			if (world.getWorldName().equalsIgnoreCase(wName))
				p.getPlayer().setGameMode(world.getGamemode());
		}
	}
	
	private void loadWorlds2() {
		//
		loadedworlds.add(new NeedsWorld(plugin.getServer().getWorlds().get(0).getName(), GameMode.SURVIVAL));
		FileConfiguration wConf = YamlConfiguration.loadConfiguration(worldsfile);
		//get our worlds
		if (wConf.get(WORLDS_WRAPPER, null) == null) {
			//wConf.set(WORLDS_WRAPPER, null);
			return;
		}
		for (String world : wConf.getConfigurationSection(WORLDS_WRAPPER).getKeys(false)) {
			//check if contents are null
			String worldStart = WORLDS_WRAPPER + "." + world;
			if (wConf.get(worldStart, null) == null) continue;
			plugin.log.i(LOG_TAG, "Setting up world \"" + world + "\".");
			WorldType type     = WorldType.getByName(wConf.getString(worldStart + ".level-type", "NORMAL"));
			Boolean structures = wConf.getBoolean(worldStart + ".gen-structures", true);
			Boolean pvp        = wConf.getBoolean(worldStart + ".pvp", true);
			GameMode gm        = GameMode.valueOf(wConf.getString(worldStart + ".gamemode", "SURVIVAL"));
			
			//generate world
			WorldCreator worldcreator = new WorldCreator(world);
			worldcreator.type(type);
			
			plugin.log.i(LOG_TAG, world + " generate Stuctures set to " + structures + ".");
			worldcreator.generateStructures(structures);
			
			plugin.getServer().createWorld(worldcreator);
			
			//pull the gamerule section up
			World worldi = plugin.getServer().getWorld(world);
			loadedworlds.add(new NeedsWorld(world, gm));
			NeedsWorld nw = this.getNeedsWorld(world);
			
			//setpvp
			worldi.setPVP(pvp);
			plugin.log.i(LOG_TAG, world + " pvp set to " + pvp + ".");
			
			//begin gamerule section
			if (wConf.get(worldStart + ".gamerule", null) != null) {
				plugin.log.i(LOG_TAG, "\"" + world + "\" finished.");
				for (String gamerule : wConf.getConfigurationSection(worldStart + ".gamerule").getKeys(false)) {
					String value = wConf.getString(worldStart + ".gamerule." + gamerule, "true");
					worldi.setGameRuleValue(gamerule, value);
					plugin.log.i(LOG_TAG, world + " gamerule " + gamerule + " set to " + value + ".");
				}
			}
			
			//now we load in allowed players.
			if (wConf.get(worldStart + "." + SECTION_PLAYERS, null) != null) {
				nw.setAllowedPlayers(new ArrayList<String>());
				for (String playerName : wConf.getConfigurationSection(worldStart + "." + SECTION_PLAYERS).getKeys(false)) {
					if (playerName == null) continue;
					nw.appendAllowedPlayer(playerName);
				}
			}
				
		}
	}
	
	public List<NeedsWorld> getWorlds() {
		return this.loadedworlds;
	}
	
	public Boolean needsWorld(String w) {
		for (NeedsWorld world : this.loadedworlds) {
			if (world.getWorldName().equalsIgnoreCase(w))
				return true;
		}
		return false;
	}
	
	public NeedsWorld getNeedsWorld(String worldname) {
		for (NeedsWorld w : this.loadedworlds) if (w.getWorldName().equalsIgnoreCase(worldname)) return w;
		return null;
	}
	
	public void teleportPlayer(NeedsPlayer p, World toWorld) {
		savePlayerLoc(p);//save users current loc
		savePlayerInv(p);//save users inv, arrmor, and exp
		p.getPlayer().setExp(0.0f);
		
		//check for gamemode
		updateGamemode(p, toWorld);
		
		getPlayerInv(p, toWorld);//load other inventory
		plugin.nms.broadcastPlayer(plugin.chatDark_AQUA + p.getName() + plugin.chatGray + " went to " + plugin.chatPurple + toWorld.getName() + plugin.chatGray + ".");
		p.justTeleport(getPlayerLoc(p, toWorld));//teleport player to world
		
	}
	
	private void savePlayerLoc(NeedsPlayer p) {
		File yamlFile          = new File(plugin.getPlayerDir(p) + File.separator + "wtp.yml");
		FileConfiguration pInv = YamlConfiguration.loadConfiguration(yamlFile);
		Location l       = p.getLocation();
		String worldName = l.getWorld().getName();
		String start     = worldName + ".loc";
		
		pInv.set(start + ".x", l.getX());
		pInv.set(start + ".y", l.getY());
		pInv.set(start + ".z", l.getZ());
		pInv.set(start + ".yaw", l.getYaw());
		pInv.set(start + ".pitch", l.getPitch());
		
		try {
			pInv.save(yamlFile);
			//plugin.log.i(LOG_TAG, "Saved file to: " + yamlFile.getAbsolutePath());
		} catch (IOException e) {
			plugin.log.e(LOG_TAG, "Could not save: " + yamlFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	private Location getPlayerLoc(NeedsPlayer p, World w) {
		File yamlFile          = new File(plugin.getPlayerDir(p) + File.separator + "wtp.yml");
		FileConfiguration pInv = YamlConfiguration.loadConfiguration(yamlFile);
		
		float xf, yf, zf, yawf, pitchf;
		Location l;
		
		String start = w.getName() + ".loc";
		Object x, y, z, yaw, pitch;
		x     = pInv.get(start + ".x", null);
		y     = pInv.get(start + ".y", null);
		z     = pInv.get(start + ".z", null);
		yaw   = pInv.get(start + ".yaw", null);
		pitch = pInv.get(start + ".pitch", null);
		
		if (x == null && y == null && z == null && yaw == null && pitch == null) {
			return w.getSpawnLocation();
		}
		
		try {
			xf     = Float.parseFloat(x.toString());
			yf     = Float.parseFloat(y.toString());
			zf     = Float.parseFloat(z.toString());
			yawf   = Float.parseFloat(yaw.toString());
			pitchf = Float.parseFloat(pitch.toString());
		}catch(Exception e) {
			e.printStackTrace();
			return w.getSpawnLocation();
		}
		
		return new Location(w, xf, yf, zf, yawf, pitchf);
	}
	
	private void savePlayerInv(NeedsPlayer p) {
		File yamlFile          = new File(plugin.getPlayerDir(p) + File.separator + "wtp.yml");
		FileConfiguration pInv = YamlConfiguration.loadConfiguration(yamlFile);
		PlayerInventory slots  = p.getPlayer().getInventory();
		
		int slotCounter = 0, armCounter = 0;
		
		String start = p.getWorld().getName() + ".inv";
		
		//save inv
		for (ItemStack slot : slots.getContents()) {
			String saveloc = start + "." + Integer.toString(slotCounter);
			pInv.set(saveloc, null);
			
			slotCounter++;
			if (slot == null) continue;
			
			//save inv
			pInv.set(saveloc + ".amt", slot.getAmount());
			pInv.set(saveloc + ".dur", Short.toString(slot.getDurability()));
			pInv.set(saveloc + ".typ", slot.getType().toString());
			int enchCounter = 0;
			for (Entry<Enchantment, Integer> enchs : slot.getEnchantments().entrySet()) {
				pInv.set(saveloc + ".enh." + enchCounter + ".nme", enchs.getKey().getName());
				pInv.set(saveloc + ".enh." + enchCounter + ".lvl", enchs.getValue());
				enchCounter++;
			}
		}
		
		//save arm
		for (ItemStack armslot : slots.getArmorContents()) {
			String armsave = start + ".arm." + Integer.toString(armCounter);
			
			pInv.set(armsave, null);
			
			armCounter++;
			if(armslot.getType().toString() == null) continue;
			
			pInv.set(armsave + ".amt", armslot.getAmount());
			pInv.set(armsave + ".dur", Short.toString(armslot.getDurability()));
			pInv.set(armsave + ".typ", armslot.getType().toString());
			int enchCounter = 0;
			for (Entry<Enchantment, Integer> enchs : armslot.getEnchantments().entrySet()) {
				pInv.set(armsave + ".enh." + enchCounter + ".nme", enchs.getKey().getName());
				pInv.set(armsave + ".enh." + enchCounter + ".lvl", enchs.getValue());
				enchCounter++;
			}
		}
		
		
		if (p.getPlayer().getExp() != 0) pInv.set(p.getWorld().getName() + ".exp", p.getPlayer().getExp());
		
		try {
			pInv.save(yamlFile);
			//plugin.log.i(LOG_TAG, "Saved file to: " + yamlFile.getAbsolutePath());
		} catch (IOException e) {
			plugin.log.e(LOG_TAG, "Could not save: " + yamlFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	private void getPlayerInv(NeedsPlayer p, World w) {
		File yamlFile          = new File(plugin.getPlayerDir(p) + File.separator + "wtp.yml");
		FileConfiguration pInv = YamlConfiguration.loadConfiguration(yamlFile);
		PlayerInventory slots  = p.getPlayer().getInventory();
		slots.clear();//clear inventory
		int slotCounter = 0;
		
		p.getPlayer().setExp(Float.parseFloat(pInv.get(w.getName() + ".exp", "0").toString()));
		
		String start = w.getName() + ".inv";
		
		//restore inventory
		for (slotCounter = 0; slotCounter < slots.getSize(); slotCounter++) {
			String fetchloc = start + "." + Integer.toString(slotCounter);
			
			if (pInv.get(fetchloc, null) == null) continue;
			
			ItemStack slot = new ItemStack(Material.AIR, 0);
			slot.setType(Material.getMaterial(pInv.getString(fetchloc + ".typ", "AIR")));
			slot.setAmount(pInv.getInt(fetchloc + ".amt", 0));
			slot.setDurability(Short.parseShort(pInv.getString(fetchloc + ".dur", "0")));
			if (pInv.get(fetchloc + ".enh", null) != null) {
				int ii = 0;
				for (ii = 0; ii < pInv.getConfigurationSection(fetchloc + ".enh").getKeys(false).size(); ii++) {
					String enchantmentname = pInv.getString(fetchloc + ".enh." + ii + ".nme", null);
					int enchantmentlevel    = pInv.getInt(fetchloc + ".enh." + ii + ".lvl", 0);
					if (enchantmentname == null || enchantmentlevel == 0) continue;
					
					//pervent errors durning encahntment restore.
					try {
						Enchantment enchant =  Enchantment.getByName(enchantmentname);
						slot.addEnchantment(enchant, enchantmentlevel);
					} catch (Exception e) {
						//send player info about enchant fail
						p.sendError("Failed to restore enchantment " + enchantmentname + " on item #" + slotCounter);
						continue;
					}
				}
			}
			slots.setItem(slotCounter, slot);
		}
		
		slots.setArmorContents(null);
		
		//restore armor
		String armstart = start + ".arm.";
		List<ItemStack> armorcontents = new ArrayList<ItemStack>();
		if (pInv.get(armstart, null) != null) {
			for (String slotnum : pInv.getConfigurationSection(armstart).getKeys(false)) {
				if (pInv.get(armstart + slotnum, null) == null) continue;
				//begin by creating an empty object
				ItemStack armorbit = new ItemStack(Material.AIR, 0);
				armorbit.setAmount(pInv.getInt(armstart + slotnum, 0));
				armorbit.setType(Material.getMaterial(pInv.getString(armstart + slotnum + ".typ", "AIR")));
				armorbit.setDurability(Short.parseShort(pInv.getString(armstart + slotnum + ".dur", "0")));
				if (pInv.get(armstart + slotnum + ".enh", null) != null) {
					int ii = 0;
					for (ii = 0; ii < pInv.getConfigurationSection(armstart + slotnum + ".enh").getKeys(false).size(); ii++) {
						String enchantmentname = pInv.getString(armstart + slotnum + ".enh." + ii + ".nme", null);
						int enchantmentlevel    = pInv.getInt(armstart + slotnum + ".enh." + ii + ".lvl", 0);
						if (enchantmentname == null || enchantmentlevel == 0) continue;
						Enchantment enchant =  Enchantment.getByName(enchantmentname);
						armorbit.addEnchantment(enchant, enchantmentlevel);
					}
				}
				armorcontents.add(armorbit);
			}
			slots.setArmorContents(armorcontents.toArray(new ItemStack[0]));
		}
	}
	
}
