package com.atelier;

import com.atelier.console.ConsoleCache;
import com.atelier.console.ConsoleCommandRegistry;
import com.atelier.console.commands.CampaignConsoleCommand;
import com.atelier.console.commands.CharacterConsoleCommand;
import com.atelier.console.commands.StopConsoleCommand;
import com.atelier.console.commands.UserConsoleCommand;
import com.atelier.discord.commands.CommandRegistry;
import com.atelier.discord.commands.WaifuCommand;
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
		ConsoleCache cache = new ConsoleCache();

		registry.registerCommand(new StopConsoleCommand());
		registry.registerCommand(new UserConsoleCommand(cache));
		registry.registerCommand(new CharacterConsoleCommand(cache));
		registry.registerCommand(new CampaignConsoleCommand(cache));
	}
}
