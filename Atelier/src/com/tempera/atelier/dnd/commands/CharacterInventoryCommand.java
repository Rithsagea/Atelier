package com.tempera.atelier.dnd.commands;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterInventoryCommand extends CharacterSubCommand {

	private MenuManager menuManager;

	public CharacterInventoryCommand(AtelierBot bot) {
		super(bot.getDatabase());

		menuManager = bot.getMenuManager();
	}

	@Override
	public String getLabel() {
		return "inventory";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("i");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(Sheet sheet, User user, List<String> args,
		MessageReceivedEvent event) {
		menuManager.addMenu(event.getChannel(),
			new CharacterInventoryMenu(sheet, menuManager));
	}

}
