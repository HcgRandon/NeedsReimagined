package com.devoverflow.reimagined.needs.commands;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;
import com.devoverflow.reimagined.needs.res.NeedsValues;

public class CommandHome implements CommandExecutor{
	private Needs plugin;
	
	public CommandHome(Needs plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("CONSOLE can not use home command");
			return true;
		}
		
		NeedsPlayer player = new NeedsPlayer(plugin, (Player)sender);
		
		if (!player.hasPermission(plugin.getCommandPerms("home"))) {
			player.sendError(NeedsValues.perm_error);
			return true;
		}
		
		File yamlFile = new File(plugin.getPlayerDir(player) + File.separator + "homes.yml");
		FileConfiguration hConf = YamlConfiguration.loadConfiguration(yamlFile);
		
		String start = player.getWorld().getName();
		
		Object x     = hConf.get(start + ".x", null);
		Object y     = hConf.get(start + ".y", null);
		Object z     = hConf.get(start + ".z", null);
		Object yaw   = hConf.get(start + ".yaw", null);
		Object pitch = hConf.get(start + ".pitch", null);
		
		if (x == null && y == null && z == null && yaw == null && pitch == null) {
			player.sendDefault("No home set, Please set one.");
		}else{
			player.teleportTo(new Location(player.getWorld(), Double.parseDouble(x.toString()), Double.parseDouble(y.toString()), Double.parseDouble(z.toString()), Float.parseFloat(yaw.toString()), Float.parseFloat(pitch.toString())));
			player.sendDefault("Taking you to your home.");
		}
		return false;
	}

}
