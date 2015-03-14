package com.talesofdertinia.ToDWhitelist;

import java.util.logging.Logger;

import com.talesofdertinia.ToDWhitelist.db.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class ToDWhitelist extends JavaPlugin implements Listener {
	static Logger logger;
	static Database db;
	
	Strategy strategy;

	@Override
	public void onEnable() {
		logger = getLogger();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		reinitialize();
		
		ToDWhitelist pl = this;
		getServer().getScheduler().runTaskTimerAsynchronously(pl, new Runnable() {
			
			@Override
			public void run() {
				if (!db.isConnected()) {
					getLogger().warning("No connection to database");
					getLogger().warning("Server is in backup mode until resolved.");
					getServer().getScheduler().runTask(pl, () -> {backup_mode();});
					try {
						db.reconnect();
						//Go back to normal mode
						getServer().getScheduler().runTaskLater(pl, () -> {normal_mode();}, 2000);
					} catch (Exception e) {
						getLogger().severe(e.getMessage());
						getLogger().severe("");
					}
				}
			}
		}, 2000, 10000);
	}
	
	void reinitialize() {
		strategy = new WhitelistStrategy();
		
		ConfigurationSection ds_config = getConfig().getConfigurationSection("datastore");
		DatabaseFactory.Settings settings = new DatabaseFactory.Settings(ds_config);
		try {
			db = DatabaseFactory.createInstance(this, ds_config.getString("type", "mysql"), settings);
		} catch (Exception e) {
			getLogger().severe("Unable to establish connection to database.\n" +
					settings);

			getLogger().severe("Putting server into backup mode");
			backup_mode();
		}

		getCommand(ToDWhitelistCommand.getCommandString()).setExecutor(new ToDWhitelistCommand(this));
		getCommand(InviteCommand.getCommandString()).setExecutor(new InviteCommand(this));
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void normal_mode() {
		getServer().setWhitelist(false);
	}

	/**
	 * Sets the server in backup mode to protect it when the database is unavailable.
	 * 
	 */
	public void backup_mode() {
		getServer().setWhitelist(true);
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
