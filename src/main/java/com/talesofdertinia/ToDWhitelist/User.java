package com.talesofdertinia.ToDWhitelist;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.talesofdertinia.ToDWhitelist.User.Status;

/**
 * Object model of user_data table.
 * Represents a user from the plugin's perspective.
 * 
 * 
 * @author Zephyyrr
 *
 */
public class User implements Serializable {

	public enum Status {
		undecided,
		pending,
		whitelisted,
		blacklisted,
		trial,
		guest;

		public static Status fromString(String s) {
			s = s.toLowerCase();
			switch (s) {
			case "blacklisted": return Status.blacklisted;
			case "guest": return Status.guest;
			case "trial": return Status.trial;
			case "whitelisted": return Status.whitelisted;
			default: return Status.pending;
			}
		}
		
		public String getList() {
			switch (this) {
			case blacklisted: return "blacklist";
			case guest: return "guestlist";
			case trial: return "triallist";
			case whitelisted: return "whitelist";
			case pending: return "list of pending users";
			default: return "nonexisting list";
			}
		}
	}

	@Id
	UUID uuid;
	Status status;
	String email;

	int invites;
	int total_invites;
	//UUID invited;


	public User(UUID uuid, Status status) {
		this.uuid = uuid;
		this.status = status;
		this.email = "";
		this.invites = 0;
		this.total_invites = 0;
	}
	
	public User(UUID uuid, Status status, String email) {
		this(uuid, status);
		this.email = email;
	}

	public User(UUID uuid, Status status, String email, int invites, int total_invites) {
		this(uuid, status, email);
		this.invites = invites;
		this.total_invites = total_invites;
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

	public Status getStatus() {
		return status;
	}
	
	public String getEmail() {
		return email;
	}

	public int getInvites() {
		return invites;
	}
	
	public int getTotalInvites() {
		return total_invites;
	}
	
	public void grantInvite() {
		invites++;
		total_invites++;
	}

	public boolean useInvite() {
		invites--;
		if (invites < 0) {
			invites = 0;
			return false;
		}
		return true;
	}
}
