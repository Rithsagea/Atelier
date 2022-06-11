package com.tempera.atelier.dnd.commands.character;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.lcommands.PermissionLevel;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterAttributeCommand extends CharacterBaseCommand {

	private MenuManager menuManager;

	public CharacterAttributeCommand(AtelierBot bot) {
		super(bot, "attribute", DataUtil.list("a"), PermissionLevel.USER);

		menuManager = bot.getMenuManager();
	}

	@Override
	public void execute(Sheet sheet, User user, List<String> args,
		MessageReceivedEvent event) {
		menuManager.addMenu(event.getChannel(),
			new CharacterAttributeMenu(sheet, menuManager));
	}

}
