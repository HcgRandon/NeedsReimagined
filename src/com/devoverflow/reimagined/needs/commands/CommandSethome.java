package com.devoverflow.reimagined.needs.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsValues;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class CommandSethome implements CommandExecutor {
	private Needs plugin;
	
	public CommandSethome(Needs plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to use sethome.");
			return true;
		}
		
		NeedsPlayer player = new NeedsPlayer(plugin, (Player)sender);
		
		if (!player.hasPermission(plugin.getCommandPerms("sethome"))) {
			player.sendError(NeedsValues.perm_error);
			plugin.log.i(plugin.LOG_TAG, player.getName() + " attempted to use sethome, but was denied access.");
			return true;
		}
		
		File homesFile = new File(plugin.getPlayerDir(player.base) + File.separator + "homes.yml");
		FileConfiguration hConf = YamlConfiguration.loadConfiguration(homesFile);
		
		Location nHome = player.base.getLocation();
		String w        = nHome.getWorld().getName();
		
		hConf.set(w + ".x", nHome.getX());
		hConf.set(w + ".y", nHome.getY());
		hConf.set(w + ".z", nHome.getZ());
		hConf.set(w + ".yaw", nHome.getYaw());
		hConf.set(w + ".pitch", nHome.getPitch());
		
		try {
			hConf.save(homesFile);
			player.sendDefault("Home set, when death befalls you you will return here.");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			player.sendError("There was an issue when I tried to set your home. Please try again later.");
			plugin.log.e(plugin.LOG_TAG, "Failed to sethome for " + player.getName());
			return true;
		}
	}

}
