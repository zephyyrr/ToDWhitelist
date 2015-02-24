package com.talesofdertinia.ToDWhitelist;

import org.bukkit.plugin.Plugin;

public class DatabaseFactory {
	public class Settings {
		final String location;
		final String username;
		
		final String password;
		
		public Settings(String location, String username, String password) {
			this.location = location;
			this.username = username;
			this.password = password;
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
