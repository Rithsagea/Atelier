package com.tempera.atelier.discord.commands;

import java.util.List;

import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface AtelierCommand {
	public String getLabel();
	public List<String> getAliases();
	public PermissionLevel getLevel();
	
	public void execute(User user, List<String> args, MessageReceivedEvent event);
}