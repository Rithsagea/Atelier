package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.discord.commands.AtelierCommand;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class MusicSubCommand implements AtelierCommand {
	
	private AtelierAudioManager audioManager;
	
	public MusicSubCommand(AtelierAudioManager audioManager) {
		this.audioManager = audioManager;
	}
	
	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		execute(audioManager.getAudioHandler(event.getGuild()), user, args, event);
	}
	
	public abstract void execute(AtelierAudioHandler audioHandler, User user, List<String> args, MessageReceivedEvent event);

}
