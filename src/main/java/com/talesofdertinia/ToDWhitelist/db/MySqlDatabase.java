package com.talesofdertinia.ToDWhitelist.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;

import com.talesofdertinia.ToDWhitelist.db.DatabaseFactory.Settings;
import com.talesofdertinia.ToDWhitelist.Invite;
import com.talesofdertinia.ToDWhitelist.ToDWhitelist;
import com.talesofdertinia.ToDWhitelist.User;
import com.talesofdertinia.ToDWhitelist.User.Status;

public class MySqlDatabase implements Database {
	
	public static UUID deformat(String uuid) {
		StringBuilder sb = new StringBuilder();
		sb.append(uuid.subSequence(0, 8));
		sb.append('-');
		sb.append(uuid.subSequence(8, 12));
		sb.append('-');
		sb.append(uuid.subSequence(12, 16));
		sb.append('-');
		sb.append(uuid.subSequence(16, 20));
		sb.append('-');
		sb.append(uuid.subSequence(20, 32));

		return UUID.fromString(sb.toString());
	}
	public static String format(UUID uuid) {
		return uuid.toString().replaceAll("-", "");
	}
	Plugin pl;
	Connection conn;
	PreparedStatement user_query;

	PreparedStatement user_insert;
	PreparedStatement user_update;
	PreparedStatement invite_query;
	PreparedStatement invite_query_all;

	PreparedStatement invite_insert;

	PreparedStatement invite_update;

	public MySqlDatabase(Plugin plugin, Settings s) throws SQLException {
		pl = plugin;
		conn = DriverManager.getConnection(s.location, s.username, s.password);

		user_query = conn
				.prepareStatement("SELECT state,email,availableInvites,totalInvites FROM "
						+ s.table + " where uuid=?;");
		user_insert = conn
				.prepareStatement("INSERT INTO "
						+ s.table
						+ " VALUES uuid=?, email=?, state=?, availableInvites=?, totalInvites=?;");
		user_update = conn
				.prepareStatement("UPDATE "
						+ s.table
						+ " SET email=?, state=?, availableInvites=?, totalInvites=? WHERE uuid=?;");

		invite_query = conn
				.prepareStatement("SELECT users.uuid, invites.recruitedGotReward, invites.recruiterGotReward, invites.dateRecruited "
						+ "FROM wp_tod_user_recruitment AS invites "
						+ "LEFT JOIN wp_tod_user_data AS users ON users.id=invites.id "
						+ "WHERE invites.recruitedUUID=?;");
		invite_query_all = conn.prepareStatement("");
		invite_insert = conn
				.prepareStatement("INSERT INTO wp_tod_user_recruitment VALUES recruitedUUID=?, id=(SELECT id FROM wp_tod_user_data WHERE uuid=?);");
		invite_update = conn
				.prepareStatement("UPDATE wp_tod_user_recruitment SET recruitedGotReward=?, recruiterGotReward=? WHERE recruitedUUID=?");
	}

	@Override
	public void finalize() {
		try {
			user_query.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			// Notify user of error and move on.
			e.printStackTrace();
			return;
		}
	}

	@Override
	public Invite getInvite(User invited) {
		try {
			invite_query.setString(1, format(invited.getUUID()));
			ResultSet rs = invite_query.executeQuery();
			while (rs.next()) {
				User inviter = getUser(deformat(rs.getString(1)));
				return new Invite(inviter, invited, new Date(rs.getTimestamp(4)
						.getTime()), rs.getBoolean(3), rs.getBoolean(2));
			}
		} catch (SQLException e) {
			ToDWhitelist.getStaticLogger().severe(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Invite> getInvites(User inviter) {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(OfflinePlayer player) {
		return getUser(player.getUniqueId());
	}

	public User getUser(UUID uuid) {
		try {
			user_query.setString(1, format(uuid));
			ResultSet rs = user_query.executeQuery();
			if (rs.next()) {
				return new User(uuid, Status.fromString(rs.getString(1)));
			}
		} catch (SQLException e) {
			ToDWhitelist.getStaticLogger().severe(e.getMessage());
		}
		// Fallback to a default behaviour.
		return new User(uuid, Status.Undecided);
	}

	@Override
	public boolean insert(Invite invite) {
		// "INSERT INTO wp_tod_user_recruitment VALUES recruitedUUID=?, id=(SELECT id FROM wp_tod_user_data WHERE uuid=?);"
		try {
			invite_insert.setString(1, format(invite.getInvited().getUUID()));
			invite_insert.setString(2, format(invite.getInviter().getUUID()));
			return invite_insert.executeUpdate() > 0;
		} catch (SQLException e) {
			ToDWhitelist.getStaticLogger().severe(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean insert(User user) {
		try {
			user_insert.setString(1, format(user.getUUID()));
			user_insert.setString(2, user.getEmail());
			user_insert.setString(3, user.getStatus().toString());
			user_insert.setInt(4, user.getInvites());
			user_insert.setInt(5, user.getTotalInvites());

			return user_insert.executeUpdate() > 0;
		} catch (SQLException e) {
			ToDWhitelist.getStaticLogger().severe(e.getMessage());
			return false;
		}

	}

	@Override
	public boolean update(Invite invite) {
		//"UPDATE wp_tod_user_recruitment SET recruitedGotReward=?, recruiterGotReward=? WHERE recruitedUUID=?"
		try {
			invite_update.setString(3, format(invite.getInvited().getUUID()));
			invite_update.setBoolean(1, invite.isInvitedRewarded());
			invite_update.setBoolean(2, invite.isInviterRewarded());
			return invite_update.executeUpdate() > 0;
		} catch (SQLException e) {
			ToDWhitelist.getStaticLogger().severe(e.getMessage());
			return false;
		}

	}

	@Override
	public boolean update(User u) {
		try {
			user_update.setString(1, u.getEmail());
			user_update.setString(2, u.getStatus().toString());
			user_update.setInt(3, u.getInvites());
			user_update.setInt(4, u.getTotalInvites());
			user_update.setString(5, format(u.getUUID()));

			return user_update.executeUpdate() > 0;
		} catch (SQLException e) {
			ToDWhitelist.getStaticLogger().severe(e.getMessage());
			return false;
		}
	}
}
