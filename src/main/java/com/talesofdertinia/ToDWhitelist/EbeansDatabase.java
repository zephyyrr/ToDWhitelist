package com.talesofdertinia.ToDWhitelist;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class EbeansDatabase implements Database {
	
	Plugin pl;
	
	public EbeansDatabase(Plugin plugin) {
		pl = plugin;
	}

	public User getUser(UUID uuid) {
		return pl.getDatabase().find(User.class, uuid);
	}

	public User getUser(OfflinePlayer player) {
		return getUser(player.getUniqueId());
	}

}
