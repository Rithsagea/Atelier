package com.tempera.atelier.dnd.commands.character;

import java.util.List;
import java.util.UUID;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.lcommands.BaseCommand;
import com.tempera.atelier.discord.lcommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterListCommand extends BaseCommand {

	private AtelierDB db;
	
	public CharacterListCommand(AtelierBot bot) {
		super("list", DataUtil.list("l"), PermissionLevel.USER);
		db = bot.getDatabase();
	}

	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		MessageBuilder mb = new MessageBuilder("Available Character Sheets:\n");
		
		for(UUID id : user.getSheets()) {
			mb.append(db.getSheet(id) + "\n");
		}
		
		event.getChannel().sendMessage(mb.build()).queue();
	}

}
