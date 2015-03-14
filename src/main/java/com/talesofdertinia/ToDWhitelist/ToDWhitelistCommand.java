package com.talesofdertinia.ToDWhitelist;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.talesofdertinia.ToDWhitelist.User.Status;
import com.talesofdertinia.ToDWhitelist.db.Database;

public class ToDWhitelistCommand implements CommandExecutor {
	
	ToDWhitelist pl;
	Database db;

	public ToDWhitelistCommand(ToDWhitelist pl) {
		this.pl = pl;
		this.db = pl.getToDDatabase();
	}

	public static String getCommandString() {
		return "todwhitelist";
	}

	public boolean onCommand(CommandSender sender ,Command command ,String commandstring, String[] args) {
		if (commandSelector(sender, commandstring, args, 0)) {
			return true;
		}
		
		if (args.length < 1) {
			return false;
		}
		
		return commandSelector(sender, args[0], args, 1);
		
	}

	private boolean commandSelector(CommandSender sender, String commandstring, String[] args,
			int consumed) {
		switch (commandstring) {
		case "whitelist":
		case "wl": return whitelist(sender, args, consumed);
		
		case "blacklist":
		case "bl": return blacklist(sender, args, consumed);
		
		case "backupmode":
		case "bm": return backupmode(sender, args, consumed);
		
		default: return false;
		}
	}
	
	private boolean backupmode(CommandSender sender, String[] args, int consumed) {
		if (args.length <= consumed) {
			return false;
		}
		
		switch(args[consumed]) {
		case "enable": 
			pl.getLogger().info("Enabled backup mode");
			sender.sendMessage("Enabled whitelist backup mode.");
			pl.backup_mode(); break;
		case "disable": 
			pl.getLogger().info("Returned to normal mode");
			sender.sendMessage("Resumed normal operation for whitelist.");
			pl.normal_mode(); break;
		default:
			return false;
		}
		
		return true;
	}

	private boolean blacklist(CommandSender sender, String[] args, int consumed) {
		return adminList(sender, args, consumed, Status.blacklisted);
	}
	
	private boolean whitelist(CommandSender sender, String[] args, int consumed) {
		return adminList(sender, args, consumed, Status.whitelisted);
	}

	public boolean adminList(CommandSender sender, String[] args, int consumed, Status list) {
		// determine instruction
		if (args.length <= consumed) {
			return false;
		}
		
		Instruction ins = null;
		switch (args[consumed].toLowerCase()) {
		case "add": 
		case "a":
			ins = (User u) -> {
				u.status = list;
				if (!db.insert(u)) {
					if (!db.update(u)) {
						throw new InstructionException("Unable to add user to the " + list.getList() + ". Please check if user is not already listed.");
					}
				}
				sender.sendMessage("Successfully added user to " + list.getList());
				pl.getLogger().info("Added " + u.getUUID() + " (" + u.getPlayer().getName() + ") to " + list.getList());
				return true;
			}; break;
		case "remove":
		case "r":
			ins = (User u) -> {
				if (u.status == Status.undecided) {
					throw new InstructionException("Unable to remove user from the " + list.getList() + ". User is not known to the system.");
				}
				if (u.status != list) {
					throw new InstructionException("Unable to remove user from the " + list.getList() + ". User is not " + list.toString() + ".");
				}
				u.status = Status.pending;
				if (!db.update(u)) {
					throw new InstructionException("Unable to remove user from the" + list.getList() + ". Please make sure the user is listed.");
				}
				sender.sendMessage("Successfully removed user from " + list.getList());
				pl.getLogger().info("Removed " + u.getUUID() + " (" + u.getPlayer().getName() + ") from " + list.getList());
				return true;
			}; break;
		default: return false;
		}		
		consumed++;
		
		// fetch player
		if (args.length <= consumed) {
			return false;
		}
		
		//This is the way to get the offlineplayer.
		//It is obvious the argument to the call is player input.
		@SuppressWarnings("deprecation") 
		OfflinePlayer player = Bukkit.getOfflinePlayer(args[consumed]);
		consumed++;
		
		// fetch user
		User u = db.getUser(player);
		
		// execute action on user
		try {
			return ins.execute(u);
		} catch (InstructionException e) {
			sender.sendMessage(e.getMessage());
			return true;
		}
	}
	
	private interface Instruction {
		boolean execute(User u) throws InstructionException;
	}
	
	private class InstructionException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7360164065039811689L;
		
		public InstructionException(String message) {
			super(message);
		}
		
	}

}
