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
		
		NeedsPlayer player = null, target = null;
		if (sender instanceof Player) {
			player = new NeedsPlayer(plugin, (Player)sender);
			if (!player.hasPermission(plugin.getCommandPerms("tp"))) {
				player.sendError("You do not have permission to preform that command.");
				return true;
			}
		}
		
		String usage = "/" + label + " [player] <x> <y> <z> or <player> <target>";
		
		if (args.length < 2) {
			if (sender instanceof Player) player.sendDefault(usage);
			else sender.sendMessage("Usage: " + usage);
			return true;
		}
		
		Float x = Float.parseFloat(args[0]);
		
		if ((args[0] != null ? Float.parseFloat(args[0]) : null) != null && args.length == 3) {
			//assume player wants to teleport himself to cords
			Float y = Float.parseFloat(args[1]);
			Float z = Float.parseFloat(args[2]);
			if (x != null && y != null && z != null) {
				if (sender instanceof Player) {
					player.teleportTo(new Location(player.base.getWorld(), x, y, z));
					player.sendDefault("You have been teleported to: " + x + ", " + y + ", " + z + ".");
					return true;
				} else {
					sender.sendMessage("You need to specify a player when you are in CONSOLE.");
					return true;
				}
			}
		}
		
		return false;
	}
}
