package com.talesofdertinia.ToDWhitelist.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.talesofdertinia.ToDWhitelist.db.DatabaseFactory.Settings;
import com.talesofdertinia.ToDWhitelist.Invite;
import com.talesofdertinia.ToDWhitelist.User;
import com.talesofdertinia.ToDWhitelist.User.Status;

public class DummyDatabase implements Database {
	Settings s;

	DummyDatabase(Settings s) {
		this.s = s;
	}

	@Override
	public User getUser(UUID uuid) {
		return new User(uuid, Status.fromString(s.table));
	}

	@Override
	public User getUser(OfflinePlayer player) {
		return getUser(player.getUniqueId());
	}

	@Override
	public boolean insert(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(User u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Invite getInvite(User invited) {
		// TODO Auto-generated method stub
		return new Invite(null, invited, new Date(), false, false);
	}

	@Override
	public List<Invite> getInvites(User inviter) {
		// TODO Auto-generated method stub
		return new ArrayList<Invite>();
	}

	@Override
	public boolean insert(Invite invite) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean update(Invite invite) {
		// TODO Auto-generated method stub
		return true;
	}

}
