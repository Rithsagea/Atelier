package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TogglePauseCommand extends MusicSubCommand{

	public TogglePauseCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "pause";
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
	public void execute(AtelierAudioHandler audioHandler, User user, List<String> args, MessageReceivedEvent event) {
		if (audioHandler.getPauseState()) {
			event.getChannel().sendMessage("Resuming").queue();
			audioHandler.setPausedState(false);
			return;
		}
		event.getChannel().sendMessage("Pausing").queue();
		audioHandler.setPausedState(true);
	}

}
