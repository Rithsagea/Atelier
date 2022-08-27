package com.atelier.discord.commands;

import com.atelier.discord.commands.music.MusicCommand;
import com.atelier.dnd.commands.CharacterCommand;

public class Commands {
	public static void registerCommands(CommandRegistry reg) {
		reg.registerCommand(new WaifuCommand());
		reg.registerCommand(new MusicCommand());
		
		//DND
		reg.registerCommand(new CharacterCommand());
	}
}
