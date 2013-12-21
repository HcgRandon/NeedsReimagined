package com.devoverflow.reimagined.needs.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class CommandWaypoint implements CommandExecutor {
	private Needs plugin;
	private int linesper = 8;
	
	public CommandWaypoint(Needs plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(NeedsValues.console_cant);
			return true;
		}
		//is player get NeedsPlayer
		NeedsPlayer player = new NeedsPlayer(plugin, (Player)sender);
		
		if (!player.hasPermission(plugin.getCommandPerms("waypoint"))) {
			player.sendError(NeedsValues.perm_error);
			return true;
		}
		
		//setup usage
		String usage = "/" + label + " <add|del|go|info|list> [wpname]";
		
		if (args.length < 1) {
			player.sendError(usage);
			return true;
		}
		
		//setup our YAML file
		File yamlFile            = new File(plugin.getPlayerDir(player) + File.separator + "wp.yml");
		FileConfiguration wpConf = YamlConfiguration.loadConfiguration(yamlFile);
		
		//get command
		String cmd = args[0].toLowerCase();
		//--------------//
		//	COMMAND ADD	//
		//--------------//
		
		if (cmd.equalsIgnoreCase("add") || cmd.equalsIgnoreCase("a")) {
			if (!player.hasPermission(plugin.getCommandPerms("waypoint") + ".add")) {
				player.sendError("You have not the permissions to add waypoints.");
				return true;
			}
			
			//check for the next args
			if (args.length < 2 || args.length > 2) {
				player.sendError(usage);
				return true;
			}
			String wpname = args[1].toLowerCase();
			
			String start = player.getWorld().getName() + "." + wpname;
//			Protect against overwrites with a confirmation message			
//			if (wpConf.get(start, null) != null) {
//				player.sendError(error)
//			}
			
			Location l = player.getLocation();
			wpConf.set(start  + ".x", l.getX());
			wpConf.set(start + ".y", l.getY());
			wpConf.set(start + ".z", l.getZ());
			wpConf.set(start + ".yaw", l.getYaw());
			wpConf.set(start + ".pitch", l.getPitch());
			
			//attempt to save new waypoint
			try {
				wpConf.save(yamlFile);
				player.sendDefault(wpname + " has been saved. To use steer clear of monsters and /" + label + " go " + wpname);
			} catch (IOException e) {
				player.sendError("Something bad happened. =c");
				e.printStackTrace();
			}
			return true;
			
		} else if (cmd.equalsIgnoreCase("delete") || cmd.equalsIgnoreCase("del") || cmd.equalsIgnoreCase("d")) {
			if (!player.hasPermission(plugin.getCommandPerms("waypoint") + ".del")) {
				player.sendError("You have not the permissions to delete waypoints.");
				return true;
			}
			
			//check for the next args
			if (args.length < 2 || args.length > 2) {
				player.sendError(usage);
				return true;
			}
			String wpname = args[1].toLowerCase();
			
			String start  = player.getWorld().getName() + "." + wpname;
			if (wpConf.get(start, null) == null) {
				wpConf.set(start, null);
				try {
					wpConf.save(yamlFile);
					player.sendDefault("Waypoint deleted!");
				} catch (IOException e) {
					player.sendError("Could not remove waypoint!");
					e.printStackTrace();
				}
			}else{
				player.sendError("Waypoint " + wpname + " does not seem to exists.");
				return true;
			}
		} else if (cmd.equalsIgnoreCase("go") || cmd.equalsIgnoreCase("g")) {
			if (!player.hasPermission(plugin.getCommandPerms("waypoint") + ".go")) {
				player.sendError("You have not the permissions to go to waypoints.");
				return true;
			}
			
			//check for the next args
			if (args.length < 2 || args.length > 2) {
				player.sendError(usage);
				return true;
			}
			String wpname = args[1].toLowerCase();
			
			String start  = player.getWorld().getName() + "." + wpname;
			
			Object x     = wpConf.get(start + ".x", null);
			Object y     = wpConf.get(start + ".y", null);
			Object z     = wpConf.get(start + ".z", null);
			Object yaw   = wpConf.get(start + ".yaw", null);
			Object pitch = wpConf.get(start + ".pitch", null);
			
			if (x == null && y == null && z == null && yaw == null && pitch == null) {
				player.sendError("That waypoint does not exists.");
				return true;
			}
			Location wploc = new Location(player.getWorld(), Double.parseDouble(x.toString()), Double.parseDouble(y.toString()), Double.parseDouble(z.toString()), Float.parseFloat(yaw.toString()), Float.parseFloat(pitch.toString()));
			
			if (player.hasAgroMob()) {
				player.sendDefault("You can't teleport right now, Monsters are nearby");
				return true;
			}
			
			player.sendDefault("To the waypoint we travel!");
			player.teleportTo(wploc);
			return true;
		} else if (cmd.equalsIgnoreCase("info") || cmd.equalsIgnoreCase("list")) {
			if (!player.hasPermission(plugin.getCommandPerms("waypoint") + ".list")) {
				player.sendError("You have not the permissions to view waypoints.");
				return true;
			}
			
			String input = Integer.toString(1);
			
			if (args.length >= 2) {
				input = args[1];
			}
			
			if (plugin.isInteger(input)) {
				int pagenum = Integer.parseInt(input);
				List<String> lines = new ArrayList<String>();
				if (wpConf.get(player.getWorld().getName(), null) == null) {
					player.sendError("I can't find waypoints for this world.");
					return true;
				}
				//add wps to it
				for (String wp : wpConf.getConfigurationSection(player.getWorld().getName()).getKeys(false)) lines.add(wp);
				//sort it
				Collections.sort(lines);
				
				
				int start, totalPages, next, end, count;
				
				if (pagenum == 1) start = 0;
				else start = ((pagenum * linesper) - linesper);
				totalPages = (int) Math.ceil((lines.size() * 1.0) / (linesper * 1.0));
				next       = pagenum + 1;
				end        = start + linesper;
				
				if (pagenum > totalPages) {
					player.sendError("No such page exists.");
					return true;
				}
				
				player.sendDefault("Page " + pagenum + " of " + totalPages);
				
				//display stuff
				for (count = 0; count < lines.size(); count++) {
					if (count >= start && count < end) {
						player.sendRaw("  " + plugin.chatDark_AQUA + lines.get(count));
					}else if (count > end) break;
				}
				
				if (pagenum < totalPages) player.sendDefault("/" + label + " list " + next + " for next page.");
				return true;
			}
		}
		
		return false;
	}
}
