package com.talesofdertinia.ToDWhitelist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;

import com.talesofdertinia.ToDWhitelist.DatabaseFactory.Settings;

public class MySqlDatabase implements Database {
	Plugin pl;
	Connection conn;
	
	MySqlDatabase(Plugin plugin, Settings s) throws SQLException {
		pl = plugin;
		conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "bukkit", "");
	}
	
	public User getUser(UUID uuid) {
		return new User(uuid);
	}
	
	public User getUser(OfflinePlayer player) {
		return getUser(player.getUniqueId());
	}
}
