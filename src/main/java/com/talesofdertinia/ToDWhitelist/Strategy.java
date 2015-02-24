package com.talesofdertinia.ToDWhitelist;

public interface Strategy {
	
	/**
	 * Determines whenever the represented user is allowed to join the server.
	 * @return true if allowed. False otherwise.
	 */
	boolean isAllowed(User user);
}
