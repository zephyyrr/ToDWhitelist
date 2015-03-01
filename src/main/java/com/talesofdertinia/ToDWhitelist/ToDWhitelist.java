package com.talesofdertinia.ToDWhitelist;

import java.util.logging.Logger;

import com.talesofdertinia.ToDWhitelist.db.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class ToDWhitelist extends JavaPlugin implements Listener {
	static Logger logger;
	static Database db;
	
	Strategy strategy;

	@Override
	public void onEnable() {
		logger = getLogger();

		getConfig().options().copyDefaults(true);
		saveConfig();

		strategy = new WhitelistStrategy();

		ConfigurationSection ds_config = getConfig().getConfigurationSection("datastore");
		DatabaseFactory.Settings settings = new DatabaseFactory.Settings(ds_config);
		try {
			db = DatabaseFactory.createInstance(this, ds_config.getString("type", "mysql"), settings);
		} catch (Exception e) {
			getLogger().severe("Unable to establish connection to database.\n" +
					settings);

			this.getPluginLoader().disablePlugin(this);
			return;
		}

		getCommand(UserControlCommand.getCommandString()).setExecutor(new UserControlCommand());
		getCommand(InviteCommand.getCommandString()).setExecutor(new InviteCommand(this));
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		db = null;
		strategy = null;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		User u = db.getUser(e.getPlayer());
		if (!strategy.isAllowed(u)) {
			e.getPlayer().kickPlayer(getConfig().getString("kickmessage"));
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
		User u = db.getUser(e.getPlayer());
		if (!strategy.isAllowed(u, e.getPlayer().getWorld())) {
			e.getPlayer().kickPlayer(getConfig().getString("kickmessage"));
		}
	}
	
	public Database getToDDatabase() {
		return db;
	}

	public static Database getStaticDatabase() {
		return db;
	}

	public static Logger getStaticLogger() {
		return logger;
	}

}
