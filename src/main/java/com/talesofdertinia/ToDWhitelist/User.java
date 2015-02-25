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
	
	public User(UUID uuid, boolean white, boolean black) {
		this.uuid = uuid;
		this.whitelisted = white;
		this.blacklisted = black;
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
