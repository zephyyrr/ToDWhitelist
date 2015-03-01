package com.talesofdertinia.ToDWhitelist;

import java.util.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class Invite {
	private UUID inviter;
	private UUID invited;
	private Timestamp time;
	private boolean invitedRewarded;
	private boolean inviterRewarded;
	
	public Invite(UUID inviter, UUID invited, Date time, 
			boolean inviterRewarded, boolean invitedRewarded) {
		this.inviter = inviter;
		this.invited = invited;
		this.time = new Timestamp(time.getTime());
		this.invitedRewarded = invitedRewarded;
		this.inviterRewarded = inviterRewarded;
	}
	
	public Invite(User inviter, User invited, Date time,
			boolean inviterRewarded, boolean invitedRewarded) {
		this(inviter.getPlayer().getUniqueId(), invited.getPlayer().getUniqueId(), 
				time, inviterRewarded, invitedRewarded);
	}

	public User getInviter() {
		return ToDWhitelist.getStaticDatabase().getUser(inviter);
	}
	
	public User getInvited() {
		return ToDWhitelist.getStaticDatabase().getUser(invited);
	}
	
	public Date getInviteTime() {
		return time;
	}
	
	public boolean isInvitedRewarded() {
		return invitedRewarded;
	}
	
	public boolean isInviterRewarded() {
		return inviterRewarded;
	}
}
