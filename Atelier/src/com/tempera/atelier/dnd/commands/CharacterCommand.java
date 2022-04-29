package com.tempera.atelier.dnd.commands;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.util.WordUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterCommand extends GroupCommand {
	
	private AtelierDB db;
	
	public CharacterCommand(AtelierBot bot) {
		db = bot.getDatabase();
		CommandRegistry registry = this.getCommandRegistry();
		registry.registerCommand(new CharacterRollCommand(db));
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
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = db.getSheet(user.getSheetId());
		
		StringBuilder builder = new StringBuilder();
		builder.append("Name: " + sheet.getName() + "\n");
		for(Ability ability : Ability.values()) {
			builder.append(String.format("%s: %d [%s]\n", 
					WordUtil.capitalize(ability.name()),
					sheet.getAbilityScore(ability),
					WordUtil.formatModifier(sheet.getAbilityModifier(ability))));
		}
		
		event.getChannel().sendMessage(builder.toString()).queue();
		
		return;
	}
}
