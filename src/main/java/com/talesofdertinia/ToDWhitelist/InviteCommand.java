package com.talesofdertinia.ToDWhitelist;

import java.util.Date;

import com.talesofdertinia.ToDWhitelist.ToDWhitelist;
import com.talesofdertinia.ToDWhitelist.User.Status;
import com.talesofdertinia.ToDWhitelist.db.Database;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class InviteCommand implements CommandExecutor {

	ToDWhitelist pl;
	Database db;
	Inviter inviter;
	Server srv;

	public InviteCommand(ToDWhitelist pl) {
		this.pl = pl;
		db = pl.getToDDatabase();
		srv = pl.getServer();
		FileConfiguration conf = pl.getConfig();
		Emailer.Settings settings = new Emailer.Settings();
		settings.username = conf.getString("mail.username");
		settings.password = conf.getString("mail.password");
		settings.host = conf.getString("mail.host");
		settings.port = (short) conf.getInt("mail.port");
		
		Emailer em = new Emailer(settings);
		pl.getLogger().info("Loading email subject: " + conf.getString("mail.subject"));
		pl.getLogger().info("Loading email message: " + conf.getString("mail.message"));
		inviter = new EmailInviter(db, em, conf.getString("mail.subject"), conf.getString("mail.message"));
	}

	public static String getCommandString() {
		return "invite";
	}

	public boolean onCommand(CommandSender sender, Command command ,String commandstring, String[] args) {
		if (args.length < 2) {
			//not enough parameters.
			return false;
		}
		
		String inviteeName = args[0];
		String email = args[1];
		
		
		//I know it is deprecated.
		//But I need to convert a name from a user to a UUID at some point.
		//The option would be to perform the query myself. Not nice.
		@SuppressWarnings("deprecation")
		User invited = new User(srv.getOfflinePlayer(inviteeName).getUniqueId(), Status.whitelisted, email, 0, 0);
		
		//TODO figure out if there is a tell of non-existing players.

		if (sender instanceof Player) {
			User u = db.getUser((Player) sender);
			Invite invite = new Invite(u, invited, 
					new Date() , false, false);
			if (u.getInvites() > 0) {
				if (!db.insert(invited)) {
					sender.sendMessage("That person is already invited!");
					return true;
				}
				
				u.useInvite();
				
				srv.getScheduler().runTaskAsynchronously(pl, () -> {
					db.update(u);
					inviter.invite(invite);
				});
			} else {
				//Not enough invites left
				sender.sendMessage("Not enough invites left.");
			}
			return true;
		} else {
			//Not a player
			sender.sendMessage("This command is currently only available to players.");
			return true;
		}

	}

}

