package com.rithsagea.atelier.discord.music;

import java.util.List;

import com.rithsagea.atelier.discord.AtelierCommand;
import com.rithsagea.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class MusicSubCommand implements AtelierCommand {
	
	private AtelierAudioManager audioManager;
	
	public MusicSubCommand(AtelierAudioManager audioManager) {
		this.audioManager = audioManager;
	}
	
	@Override
	public final void execute(User user, List<String> args, MessageReceivedEvent event) {
		AtelierAudioHandler handler = audioManager.getAudioHandler(event.getGuild());
		execute(user, handler, args, event);
	}
	
	public abstract void execute(User user, AtelierAudioHandler audioHandler, List<String> args, MessageReceivedEvent event);
}
