package com.talesofdertinia.ToDWhitelist;

public class WhitelistStrategy implements Strategy {

	/**
	 * {@inheritDoc}
	 */
	public boolean isAllowed(User user) {
		return user.inWhitelist();
	}

}
