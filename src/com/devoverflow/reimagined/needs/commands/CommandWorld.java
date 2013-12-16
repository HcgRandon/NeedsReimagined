package com.devoverflow.reimagined.needs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;
import com.devoverflow.reimagined.needs.res.NeedsWorld;

public class CommandWorld implements CommandExecutor {
	private Needs plugin;
	
	public CommandWorld(Needs plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String usage = "/" + label + " <target> <go> <worldname> or <list|go|info> [world]";
		
		NeedsPlayer player = null;
		if (sender instanceof Player) {
			player = new NeedsPlayer(plugin, (Player)sender);
		}
		
		if (args.length < 1) {
			if (sender instanceof Player) player.sendError(usage);
			else sender.sendMessage("Usage: " + usage);
			return true;
		}
		
		String command = args[0].toLowerCase();
		
		if (command == null) {
			if (sender instanceof Player) player.sendError(usage);
			else sender.sendMessage("Usage: " + usage);
			return true;
		} else if (command.equalsIgnoreCase("list")) {
			StringBuilder builder = new StringBuilder();
			int size = plugin.nwm.getWorlds().size(), i = 0;
			for (NeedsWorld world : plugin.nwm.getWorlds()) {
				i++; builder.append(world.getWorldName());
				if (size == i) builder.append(".");
				else builder.append(", ");
			}
			if (sender instanceof Player) player.sendDefault(builder.toString());
			else sender.sendMessage(builder.toString());
			
			return true;
		} else if (command.equalsIgnoreCase("go")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You must be a player to use /world go");
				return true;
			}
			
			if (args.length < 2) {
				player.sendError(usage);
				return true;
			}
			
			if (!plugin.nwm.needsWorld(args[1])) {
				player.sendError("Im sorry but I do not handle that world and cannot teleport you to it.");
				return true;
			}
			
			if (player.getWorld().getName().equalsIgnoreCase(args[1])) {
				player.sendDefault("You are already in " + plugin.chatPurple + args[1] + plugin.chatRed + ".");
				return true;
			}
			
			plugin.nwm.teleportPlayer(player.getPlayer(), plugin.getServer().getWorld(args[1]));
			player.sendDefault("You have been teleported to " + plugin.chatPurple + args[1] + plugin.chatGold + ".");
			return true;
		} else if (command.equalsIgnoreCase("info")) {
			
		} else {
			//assume it is a player
			if (plugin.getServer().getPlayer(args[0]) == null) {
				if (sender instanceof Player) player.sendError(args[0] + " does not seem to be online.");
				else sender.sendMessage(args[0] + " does not seem to be online.");
				return true;
			}
			Player target = plugin.getServer().getPlayer(args[0]);
			//we know we are targeting a player lets do it
			if (args[1].equalsIgnoreCase("go")) {
				if (!plugin.nwm.needsWorld(args[2])) {
					player.sendError("Im sorry but I do not handle that world and cannot teleport you to it.");
					return true;
				}
				plugin.nwm.teleportPlayer(target, plugin.getServer().getWorld(args[2]));
			}
			
		}
		return false;
	}
}
