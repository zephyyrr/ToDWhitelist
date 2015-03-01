package com.talesofdertinia.ToDWhitelist;

import org.bukkit.World;

import com.talesofdertinia.ToDWhitelist.User.Status;

public interface Strategy {
	
	/**
	 * Determines whenever the represented user is allowed to join the server.
	 * @return true if allowed. False otherwise.
	 */
	boolean isAllowed(User user);
	boolean isAllowed(User u, World world);
	
	Status getInviteStatus();
}
