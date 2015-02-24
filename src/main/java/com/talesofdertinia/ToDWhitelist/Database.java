package com.talesofdertinia.ToDWhitelist;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

public interface Database {
	public User getUser(UUID uuid);
	public User getUser(OfflinePlayer player);
}
