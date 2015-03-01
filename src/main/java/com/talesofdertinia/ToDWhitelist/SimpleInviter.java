package com.talesofdertinia.ToDWhitelist;

import com.talesofdertinia.ToDWhitelist.db.Database;

public class SimpleInviter implements Inviter {

	Database db;

	public SimpleInviter(Database db) {
		this.db = db;
	}

	@Override
	public boolean invite(Invite invite) {
		if (!db.insert(invite.getInvited())) {
			return false;
		}
		return db.insert(invite);
	}

}
