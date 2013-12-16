package com.devoverflow.reimagined.needs;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.devoverflow.reimagined.needs.commands.*;
import com.devoverflow.reimagined.needs.listeners.NeedsPlayerRespawnListener;
import com.devoverflow.reimagined.needs.managers.*;
import com.devoverflow.reimagined.needs.res.NeedsLogger;

public class Needs extends JavaPlugin{
	public static final String STATIC_LOG_TAG = "Needs Reimagined";
	public String LOG_TAG;
	public NeedsLogger log            = new NeedsLogger();
	
	public NeedsWorldManager nwm;
	public NeedsMessageSender nms;
	public NeedsBackManager nbm;
	
	private PluginDescriptionFile pdf;
	
	public ChatColor chatGreen     = ChatColor.GREEN;
	public ChatColor chatYellow    = ChatColor.YELLOW;
	public ChatColor chatBlue      = ChatColor.BLUE;
	public ChatColor chatGold      = ChatColor.GOLD;
	public ChatColor chatPurple    = ChatColor.DARK_PURPLE;
	public ChatColor chatWhite     = ChatColor.WHITE;
	public ChatColor chatBlack     = ChatColor.BLACK;
	public ChatColor chatRed       = ChatColor.RED;
	public ChatColor chatMagic     = ChatColor.MAGIC;
	public ChatColor chatDark_AQUA = ChatColor.DARK_AQUA;
	public ChatColor chatGray      = ChatColor.GRAY;
	public ChatColor chatBold      = ChatColor.BOLD;
	public ChatColor chatST        = ChatColor.STRIKETHROUGH;
	public ChatColor chatItalic    = ChatColor.ITALIC;
	public ChatColor chatUnderline = ChatColor.UNDERLINE;
	
	public void onEnable() {
		pdf      = getDescription();             //get pdf file
		LOG_TAG  = pdf.getName();
		nwm      = new NeedsWorldManager(this);  //startup world manager
		nms      = new NeedsMessageSender(this); //startup Message Sender
		nbm      = new NeedsBackManager(this);
		
		getCommand("back").setExecutor(new CommandBack(this));
		getCommand("tp").setExecutor(new CommandTp(this));
		getCommand("tpto").setExecutor(new CommandTpto(this));
		getCommand("tphere").setExecutor(new CommandTphere(this));
		getCommand("world").setExecutor(new CommandWorld(this));
		getCommand("whereami").setExecutor(new CommandWhereami(this));
		getCommand("sethome").setExecutor(new CommandSethome(this));
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new NeedsPlayerRespawnListener(this), this);
		pm.registerEvents(nbm, this); //register back events
		
		log.i(LOG_TAG, "Needs is now enabled");
	}
	
	public void onDisable() {
		log.i(LOG_TAG, "Needs is now disabled");
	}
	
	@SuppressWarnings("unused")
	private void shutdownplugin() {
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public boolean isFloat(String string){
		try {
			Float.parseFloat(string);
			return true;
		} catch (NumberFormatException ex){
			return false;
		}
	}
	
	public File getPlayerDir(Player p) {
		File playerDir = new File(getDataFolder() + File.separator + "players" + File.separator + p.getName());
		if (!playerDir.exists()) playerDir.mkdirs();
		
		return playerDir;
	}
	
	public String getCommandPerms(String cmdName) {
		String cmd = null; Map<String, Object> addit = null;
		for (Entry<String, Map<String, Object>> command : pdf.getCommands().entrySet()) {
			cmd = null; addit = null;
			if (command.getKey().equalsIgnoreCase(cmdName)) {
				cmd       = command.getKey();
				addit     = command.getValue();
				break;
			}
			continue;
		}
		if (cmd.isEmpty() || addit.isEmpty()) return null;
		//safe to continue with return
		if (addit.containsKey("perm")) return addit.get("perm").toString();
		else if (addit.containsKey("perms")) return addit.get("perms").toString();
		
		return null;
	}
}
