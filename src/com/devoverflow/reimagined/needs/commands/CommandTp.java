package com.devoverflow.reimagined.needs.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class CommandTp implements CommandExecutor {
	private Needs plugin;
	
	public CommandTp(Needs plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		NeedsPlayer player = null;
		if (sender instanceof Player) {
			player = new NeedsPlayer(plugin, (Player)sender);
			if (!player.hasPermission(plugin.getCommandPerms("tp"))) {
				player.sendError("You do not have permission to preform that command.");
				return true;
			}
		}
		
		String usage = "/" + label + " [player] <x> <y> <z> or <player> <target>";
		
		if (args.length < 1) {
			if (sender instanceof Player) player.sendDefault(usage);
			else sender.sendMessage("Usage: " + usage);
			return true;
		}
		
		// /tp 10 10 10
		if (plugin.isFloat(args[0]) && plugin.isFloat(args[1]) && plugin.isFloat(args[2])) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("CONSOLE must specify a player when x y z coord.");
				return true;
			}
			
			player.sendDefault("Taking you to " + args[0] + ", " + args[1] + ", " + args[2]);
			player.teleportTo(new Location(player.base.getWorld(), Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), player.base.getLocation().getYaw(), player.base.getLocation().getPitch()));
		// /tp HcgRandon 10 10 10
		} else if (!plugin.isFloat(args[0]) && plugin.isFloat(args[1]) && plugin.isFloat(args[2]) && plugin.isFloat(args[3])) {
			if (plugin.getServer().getPlayer(args[0]) == null) {
				if (sender instanceof Player) player.sendError(args[0] + " does not appear to be on the server.");
				else sender.sendMessage(args[0] + " does not appear to be on the server.");
				return true;
			}
			
			NeedsPlayer target = new NeedsPlayer(plugin, plugin.getServer().getPlayer(args[0]));
			
			float x = Float.parseFloat(args[1]), y = Float.parseFloat(args[2]), z = Float.parseFloat(args[3]);
			
			Location tpLoc = new Location(target.base.getWorld(), x, y, z, target.base.getLocation().getYaw(), target.base.getLocation().getPitch());
			
			target.sendDefault("Taking you to " + x + ", " + y + ", " + z);
			if (sender instanceof Player) player.sendDefault("Teleported " + args[0] + " to " + x + ", " + y + ", " + z);
			else sender.sendMessage("Teleported " + args[0] + " to " + x + ", " + y + ", " + z);
			
			target.teleportTo(tpLoc);
			return true;
		} else if (!plugin.isFloat(args[0]) && !plugin.isFloat(args[1])) {
			if (plugin.getServer().getPlayer(args[0]) == null) {
				if (sender instanceof Player) player.sendError(args[0] + " does not appear to be on the server.");
				else sender.sendMessage(args[0] + " does not appear to be on the server.");
				return true;
			}
			if (plugin.getServer().getPlayer(args[1]) == null) {
				if (sender instanceof Player) player.sendError(args[1] + " does not appear to be on the server.");
				else sender.sendMessage(args[1] + " does not appear to be on the server.");
				return true;
			}
			NeedsPlayer tobetp = new NeedsPlayer(plugin, plugin.getServer().getPlayer(args[0]));
			NeedsPlayer target = new NeedsPlayer(plugin, plugin.getServer().getPlayer(args[1]));
			
			if (tobetp.getName() == target.getName()) {
				if (sender instanceof Player) player.sendError("That is the same player.");
				else sender.sendMessage("That is the same player.");
				return true;
			}
			
			if (!(sender instanceof Player)) {
				sender.sendMessage(tobetp.getName() + " was teleported to " + target.getName() + "'s location.");
			}else if (player == tobetp) {
				player.sendDefault("Taking you to " + plugin.chatDark_AQUA + target.getName() + plugin.chatGold + ".");
			}else if (player == target) {
				
			}else{
				if (sender instanceof Player) player.sendError(tobetp.getName() + " was teleported to " + target.getName() + "'s location.");
				else sender.sendMessage(tobetp.getName() + " was teleported to " + target.getName() + "'s location.");
			}
			
			
			target.sendDefault("Bringing " + plugin.chatDark_AQUA + tobetp.getName() + plugin.chatGold + " here.");
			tobetp.teleportTo(target.base.getLocation());
		}
		
		return false;
	}
}
