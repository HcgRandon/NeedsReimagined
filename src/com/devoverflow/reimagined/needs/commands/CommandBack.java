package com.devoverflow.reimagined.needs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;
import com.devoverflow.reimagined.needs.res.NeedsValues;

public class CommandBack implements CommandExecutor {
	private Needs plugin;
	
	public CommandBack(Needs plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("CONSOLE can not use /back.");
			return true;
		}
		
		NeedsPlayer player = new NeedsPlayer(plugin, (Player)sender);
		
		if (!player.hasPermission(plugin.getCommandPerms("back"))) {
			player.sendError(NeedsValues.perm_error);
			plugin.log.i(plugin.LOG_TAG, "/back was denied.");
			return true;
		}
		
		plugin.nbm.teleportPlayerBack(player);
		return true;
	}
}
