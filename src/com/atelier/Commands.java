package com.atelier;

import com.atelier.console.ConsoleCache;
import com.atelier.console.ConsoleCommandRegistry;
import com.atelier.console.commands.CampaignConsoleCommand;
import com.atelier.console.commands.CharacterConsoleCommand;
import com.atelier.console.commands.SceneConsoleCommand;
import com.atelier.console.commands.SessionConsoleCommand;
import com.atelier.console.commands.StopConsoleCommand;
import com.atelier.console.commands.UserConsoleCommand;
import com.atelier.discord.commands.CommandRegistry;
import com.atelier.discord.commands.WaifuCommand;
import com.atelier.discord.commands.dnd.AttributeCommand;
import com.atelier.discord.commands.dnd.CharacterCommand;
import com.atelier.discord.commands.dnd.RollCommand;
import com.atelier.discord.commands.dnd.SessionCommand;
import com.atelier.discord.commands.music.MusicCommand;

public class Commands {
	public static void registerDiscordCommands(CommandRegistry reg) {
		reg.registerCommand(new WaifuCommand());
		reg.registerCommand(new MusicCommand());
		
		//DND
		reg.registerCommand(new CharacterCommand());
		reg.registerCommand(new AttributeCommand());
		reg.registerCommand(new RollCommand());

		//DND Admin
		reg.registerCommand(new SessionCommand());
	}
	
	public static void registerConsoleCommands(ConsoleCommandRegistry registry) {
		ConsoleCache cache = ConsoleCache.getInstance();

		registry.registerCommand(new StopConsoleCommand());
		registry.registerCommand(new UserConsoleCommand(cache));
		registry.registerCommand(new CharacterConsoleCommand(cache));
		registry.registerCommand(new CampaignConsoleCommand(cache));
		registry.registerCommand(new SceneConsoleCommand(cache));
		registry.registerCommand(new SessionConsoleCommand(cache));
	}
}
