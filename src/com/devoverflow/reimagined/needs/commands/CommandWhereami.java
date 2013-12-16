package com.devoverflow.reimagined.needs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class CommandWhereami implements CommandExecutor {
	private Needs plugin;
	
	public CommandWhereami(Needs plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		NeedsPlayer player = null;
		if (sender instanceof Player) {
			player = new NeedsPlayer(plugin, (Player)sender);
			if (!player.hasPermission(plugin.getCommandPerms("whereami"))) {
				player.sendError("You do not have permission to preform that command.");
				return true;
			}
		}
		
		
		if (args.length > 0) {
			if (plugin.getServer().getPlayer(args[0]) != null) {
				Player target = plugin.getServer().getPlayer(args[0]);
				if (sender instanceof Player) player.sendDefault(plugin.chatDark_AQUA + args[0] + plugin.chatGold +  " is in " + plugin.chatDark_AQUA + target.getWorld().getName() + plugin.chatGold + ".");
				else sender.sendMessage(args[0] + " is in " + target.getWorld().getName());
				return true;
			}
		}else{
			player.sendDefault("You are in " + plugin.chatPurple + player.getWorld().getName() + plugin.chatGold + ".");
			return true;
		}
		return false;
	}
}
