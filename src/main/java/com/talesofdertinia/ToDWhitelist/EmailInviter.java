package com.talesofdertinia.ToDWhitelist;

import java.util.UUID;

import javax.mail.MessagingException;

import com.talesofdertinia.ToDWhitelist.User.Status;
import com.talesofdertinia.ToDWhitelist.db.Database;

public class EmailInviter extends SimpleInviter {

	private final String subject;
	private final String message;
	Emailer em;
	
	public EmailInviter(Database db, Emailer em, String subject, String message) {
		super(db);
		this.em = em;
		this.subject = subject;
		this.message = message;
	}

	@Override
	public boolean invite(Invite invite) {
		if (super.invite(invite)) {
			try {
				User invited = invite.getInvited();
				String invitedName = invited.getPlayer().getName();
				
				String local_message = message;
				local_message = local_message.replaceAll("%inviter%", invite.getInviter().getPlayer().getName());
				local_message = local_message.replaceAll("%invited%", invitedName);
				
				
				em.send(invite.getInvited().getEmail(), subject, local_message);
				ToDWhitelist.getStaticLogger().info("Sent email to " + invited.getEmail() + " inviting "+ invitedName);
			} catch (MessagingException e) {
				ToDWhitelist.getStaticLogger().severe(e.getMessage());
				return false;
			}
			return true;
		}
		return false;
	}
	
}
