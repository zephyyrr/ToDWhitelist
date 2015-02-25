package com.talesofdertinia.ToDWhitelist;

import org.bukkit.World;

public class WhitelistStrategy implements Strategy {

	/**
	 * {@inheritDoc}
	 */
	public boolean isAllowed(User user) {
		return user.inWhitelist();
	}
	
	public boolean isAllowed(User user, World w) {
		return isAllowed(user);
	}

}
