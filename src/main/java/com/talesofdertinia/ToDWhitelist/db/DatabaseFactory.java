package com.talesofdertinia.ToDWhitelist.db;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class DatabaseFactory {
	public static class Settings {
		final String location;
		final String username;
		final String password;
		final String table;

		public Settings(String location, String table, String username, String password) {
			this.location = location;
			this.username = username;
			this.password = password;
			this.table    = table;
		}

		public Settings(ConfigurationSection s) {
			location = s.getString("url", "jdbc://mysql://localhost");
			username = s.getString("user");
			password = s.getString("password");
			table = s.getString("table", "users");
		}

		public String toString() {
			String userPart = (username != null  && username != "") ? username + "@" : "";
			String tablePart = (table != null  && table != "") ? ":" + table : "";
			return userPart + location + tablePart;
		}
	}

	public static Database createInstance(Plugin pl, String type, Settings s) throws Exception {
		switch (type.toLowerCase()) {
		case "mysql": return new MySqlDatabase(pl, s);
		case "dummy": return new DummyDatabase(s);
		default: throw new IllegalArgumentException(type);
		}
	}
}
