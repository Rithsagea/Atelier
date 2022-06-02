package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoopCommand extends MusicSubCommand {

	public LoopCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "loop";
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
	public void execute(AtelierAudioHandler audioHandler, User user,
		List<String> args, MessageReceivedEvent event) {
		event.getChannel()
			.sendMessage(
				audioHandler.toggleLoop() ? "Loop enabled." : "Loop disabled.")
			.queue();
	}

}
