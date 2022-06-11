package com.tempera.atelier.discord.commands.character;

import com.tempera.atelier.discord.commands.SlashGroupCommand;

public class SlashCharacterCommand extends SlashGroupCommand {

	public SlashCharacterCommand() {
		super("character", "General utility command for player characters");
		
		registerSubcommand(new SlashCharacterListCommand());
		registerSubcommand(new SlashCharacterInfoCommand());
		registerSubcommand(new SlashCharacterSelectCommand());
	}
}
