package com.talesofdertinia.ToDWhitelist;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ToDWhitelist extends JavaPlugin implements Listener {
	
	private static final String SIGNUP_ADDRESS = "http://join.talesofdertinia.com";
	Database db;
	Strategy strategy;
	
	@Override
	public void onEnable() {
		try {
			db = DatabaseFactory.createInstance(this, "jdbc", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.onDisable();
		}
		strategy = new WhitelistStrategy();
	}
	
	public void onPlayerJoin(PlayerJoinEvent e) {
		User u = db.getUser(e.getPlayer());
		if (!strategy.isAllowed(u)) {
			e.getPlayer().kickPlayer("You are not on the whitelist.\n+"
					+ "Join at " + SIGNUP_ADDRESS);
		}
	}
	
}
