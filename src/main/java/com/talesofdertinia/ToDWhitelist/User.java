package com.talesofdertinia.ToDWhitelist;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class User implements Serializable {
	
	UUID uuid;
	boolean whitelisted, blacklisted;
	
	
	public User(UUID uuid) {
		this.uuid = uuid;
		whitelisted = false;
		blacklisted = false;
	}

	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}
	
	/**
	 * Determines whenever the represented user is allowed to join the server.
	 * @return true if allowed. False otherwise.
	 */
	public boolean inBlacklist() {
		return blacklisted;
	}

	public boolean inWhitelist() {
		return whitelisted;
	}
}
