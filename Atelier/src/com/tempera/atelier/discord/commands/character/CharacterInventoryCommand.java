package com.tempera.atelier.discord.commands.character;

import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.BaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CharacterInventoryCommand extends BaseSubcommand {

	public CharacterInventoryCommand() {
		super("inventory", "Gets the character's inventory");
	}

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		MenuManager.getInstance().addMenu(new CharacterInventoryMenu(user.getSheet()), true, event);
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		
	}

}
