package com.talesofdertinia.ToDWhitelist;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class DatabaseFactory {
	public static class Settings {
		final String location;
		final String username;
		final String password;
		
		public Settings(String location, String username, String password) {
			this.location = location;
			this.username = username;
			this.password = password;
		}
		
		public Settings(ConfigurationSection s) {
			location = s.getString("url");
			username = s.getString("user");
			password = s.getString("password");
		}
	}

	static Database createInstance(Plugin pl, String type, Settings s) throws Exception {
		switch (type.toLowerCase()) {
		case "bukkit": return new EbeansDatabase(pl);
		case "mysql": return new MySqlDatabase(pl, s);
		default: throw new IllegalArgumentException(type);
		}
	}
}
