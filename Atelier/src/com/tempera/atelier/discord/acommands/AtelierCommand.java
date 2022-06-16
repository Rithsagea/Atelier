package com.tempera.atelier.discord.acommands;

import java.util.List;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface AtelierCommand {
	public String getLabel();

	public List<String> getAliases();

	public PermissionLevel getLevel();

	public void execute(User user, List<String> args, MessageReceivedEvent event);
}