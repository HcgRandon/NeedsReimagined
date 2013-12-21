package com.devoverflow.reimagined.needs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.NeedsPlayer;

public class CommandSpawn implements CommandExecutor{
	private Needs plugin;
	
	public CommandSpawn(Needs plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to preform this command.");
			return true;
		}
		
		NeedsPlayer player = new NeedsPlayer(plugin, (Player)sender);
		
		player.sendDefault("To spawn we go!");
		player.teleportTo(player.getWorld().getSpawnLocation());
		
		return false;
	}
}
