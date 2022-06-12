package com.tempera.atelier.dnd.acommands.character;

import java.util.List;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.CommandRegistry;
import com.tempera.atelier.discord.acommands.GroupCommand;
import com.tempera.atelier.discord.acommands.PermissionLevel;
import com.tempera.atelier.discord.commands.character.CharacterInfoMessageBuilder;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterCommand extends GroupCommand {

	private AtelierDB db;

	public CharacterCommand(AtelierBot bot) {
		super("character", DataUtil.list("char", "dnd"), PermissionLevel.USER);
		
		db = bot.getDatabase();

		CommandRegistry registry = this.getCommandRegistry();
		registry.registerCommand(new CharacterRollCommand(bot));
		registry.registerCommand(new CharacterInventoryCommand(bot));
	}

	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = user.getSheet();
		event.getChannel().sendMessage(new CharacterInfoMessageBuilder(sheet).build()).queue();
	}
}
