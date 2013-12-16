package com.devoverflow.reimagined.needs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsValues;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class CommandTphere implements CommandExecutor{
	private Needs plugin;
	
	public CommandTphere(Needs plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("CONSOLE can not use this feature of " + plugin.LOG_TAG);
			return true;
		}
		
		String usage = "/" + label + " [target]";
		
		NeedsPlayer player = new NeedsPlayer(plugin, (Player)sender);
		if (!player.hasPermission(plugin.getCommandPerms("tphere"))) {
			player.sendError(NeedsValues.perm_error);
			return true;
		}
		
		if (args.length < 1) {
			player.sendError(usage);
			return true;
		}
		
		if (plugin.getServer().getPlayer(args[0]) != null) {
			NeedsPlayer target = new NeedsPlayer(plugin, plugin.getServer().getPlayer(args[0]));
			
			if (player.getPlayer() == target.getPlayer()) {
				player.sendError("You are already here!");
				return true;
			}
			
			player.sendDefault("Bringing " + plugin.chatDark_AQUA + player.getName() + plugin.chatGold + ".");
			target.sendDefault("Taking you to " + plugin.chatDark_AQUA + target.getName() + plugin.chatGold + ".");
			
			plugin.log.i(plugin.LOG_TAG, sender.getName() + ": Teleported " + target.getName() + " to " + player.getName());
			
			target.teleportTo(player.getLocation());
			return true;
		} else {
			player.sendError(plugin.chatDark_AQUA + args[0] + plugin.chatRed + " was not found on the server.");
			return true;
		}
	}
}
