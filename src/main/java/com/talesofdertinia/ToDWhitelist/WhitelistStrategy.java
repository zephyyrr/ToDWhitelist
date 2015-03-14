package com.talesofdertinia.ToDWhitelist;

import org.bukkit.World;

import com.talesofdertinia.ToDWhitelist.User.Status;

public class WhitelistStrategy implements Strategy {

	/**
	 * {@inheritDoc}
	 */
	public boolean isAllowed(User user) {
		if (user.getStatus() == Status.whitelisted) {
			return true;
		} else if (user.getPlayer().isOp()) {
			ToDWhitelist.getStaticLogger().warning("Non-whitelisted Op joined. Remember to register all ops in whitelist or deop if no longer welcome.");
			return true;
		}
		return false;
	}

	public boolean isAllowed(User user, World w) {
		return isAllowed(user);
	}

	@Override
	public Status getInviteStatus() {
		//FIXME Perhaps move into separate class or passed as constructor parameter.
		//Default behaviour Assume good.
		return Status.whitelisted;
	}

}
