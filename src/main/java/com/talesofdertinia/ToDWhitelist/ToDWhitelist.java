package com.talesofdertinia.ToDWhitelist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class ToDWhitelist extends JavaPlugin implements Listener {
	Database db;
	Strategy strategy;
	
	@Override
	public void onEnable() {
		strategy = new WhitelistStrategy();
		
		ConfigurationSection ds_config = getConfig().getConfigurationSection("datastore");
		DatabaseFactory.Settings settings = new DatabaseFactory.Settings(ds_config);
		try {
			db = DatabaseFactory.createInstance(this, ds_config.getString("type", "bukkit"), settings);
		} catch (Exception e) {
			getLogger().severe("Unable to establish connection to database.\n" +
					settings.location);
			
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		getServer().getPluginManager().registerEvents(this, this);
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
	
}
