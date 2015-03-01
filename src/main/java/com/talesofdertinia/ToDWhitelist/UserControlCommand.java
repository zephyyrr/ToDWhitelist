package com.talesofdertinia.ToDWhitelist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserControlCommand implements CommandExecutor {

	public static String getCommandString() {
		return "usercontrol";
	}

	public boolean onCommand(CommandSender sender ,Command command ,String commandstring, String[] args) {
		return false;

	}

}
