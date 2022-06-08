package com.tempera.atelier.dnd.commands.character;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterCommand extends GroupCommand {

	private AtelierDB db;

	public CharacterCommand(AtelierBot bot) {
		db = bot.getDatabase();

		CommandRegistry registry = this.getCommandRegistry();
		registry.registerCommand(new CharacterRollCommand(bot));
		registry.registerCommand(new CharacterInfoCommand(db));
		registry.registerCommand(new CharacterAttributeCommand(bot));
		registry.registerCommand(new CharacterInventoryCommand(bot));
	}

	@Override
	public String getLabel() {
		return "character";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("char", "dnd");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void executeDefault(User user, List<String> args,
		MessageReceivedEvent event) {
		Sheet sheet = db.getSheet(user.getSheetId());
		event.getChannel()
			.sendMessage(new CharacterInfoMessageBuilder(sheet).build())
			.queue();
	}
}