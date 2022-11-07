package com.atelier.discord.commands;

import com.atelier.console.ConsoleCommandRegistry;
import com.atelier.console.commands.CharacterConsoleCommand;
import com.atelier.console.commands.StopConsoleCommand;
import com.atelier.console.commands.UserConsoleCommand;
import com.atelier.discord.commands.dnd.AttributeCommand;
import com.atelier.discord.commands.dnd.CharacterCommand;
import com.atelier.discord.commands.dnd.RollCommand;
import com.atelier.discord.commands.music.MusicCommand;

public class Commands {
	public static void registerDiscordCommands(CommandRegistry reg) {
		reg.registerCommand(new WaifuCommand());
		reg.registerCommand(new MusicCommand());
		
		//DND
		reg.registerCommand(new CharacterCommand());
		reg.registerCommand(new AttributeCommand());
		reg.registerCommand(new RollCommand());
	}
	
	public static void registerConsoleCommands(ConsoleCommandRegistry registry) {
		registry.registerCommand(new StopConsoleCommand());
		registry.registerCommand(new UserConsoleCommand());
		registry.registerCommand(new CharacterConsoleCommand());
	}
}
