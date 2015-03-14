package com.talesofdertinia.ToDWhitelist.db;

import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.talesofdertinia.ToDWhitelist.Invite;
import com.talesofdertinia.ToDWhitelist.User;
import com.talesofdertinia.ToDWhitelist.User.Status;

public interface Database {
	public boolean isConnected();
	public void reconnect() throws Exception;
	
	public User getUser(UUID uuid);
	public User getUser(OfflinePlayer player);
	public boolean insert(User user);
	public boolean update(User u);
	
	public Invite getInvite(User invited);
	public List<Invite> getInvites(User inviter);
	public boolean insert(Invite invite);
	public boolean update(Invite invite);
}
