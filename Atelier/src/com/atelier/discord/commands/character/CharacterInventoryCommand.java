package com.atelier.discord.commands.character;

import com.atelier.discord.MenuManager;
import com.atelier.discord.User;
import com.atelier.discord.commands.BaseInteraction.BaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CharacterInventoryCommand extends BaseSubcommand {
	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		MenuManager.getInstance().addMenu(new CharacterInventoryMenu(user.getSheet()), true, event);
	}

}
