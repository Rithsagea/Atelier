package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.lcommands.AtelierCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class MusicSubCommand implements AtelierCommand {

	private AtelierAudioManager audioManager;

	public MusicSubCommand(AtelierAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	@Override
	public void execute(User user, List<String> args,
		MessageReceivedEvent event) {
		execute(audioManager.getAudioHandler(event), user, args, event);
	}

	public abstract void execute(AtelierAudioHandler audioHandler, User user,
		List<String> args, MessageReceivedEvent event);

}
