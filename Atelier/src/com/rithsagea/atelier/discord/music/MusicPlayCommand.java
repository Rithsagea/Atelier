package com.rithsagea.atelier.discord.music;

import java.util.List;

import com.rithsagea.atelier.discord.AtelierCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicPlayCommand implements AtelierCommand {

	private AtelierAudioManager audioManager;
	
	public MusicPlayCommand(AtelierAudioManager audioManager) {
		this.audioManager = audioManager;
	}
	
	@Override
	public String getLabel() {
		return "play";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public void execute(List<String> args, MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		AtelierAudioHandler audioHandler = audioManager.getAudioHandler(guild);
		
		audioHandler.loadTrack(args.get(1));
	}

}
