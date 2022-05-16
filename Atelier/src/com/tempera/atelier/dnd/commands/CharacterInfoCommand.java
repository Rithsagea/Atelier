package com.tempera.atelier.dnd.commands;

import java.util.List;

import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.User;
import com.tempera.atelier.dnd.database.AtelierDB;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterInfoCommand extends CharacterSubCommand{

	public CharacterInfoCommand(AtelierDB db) {
		super(db);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getLabel() {
		return "info";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(Sheet sheet, User user, List<String> args, MessageReceivedEvent event) {
		event.getChannel().sendMessage(new InfoMessageBuilder(sheet).build()).queue();
		
	}

}
