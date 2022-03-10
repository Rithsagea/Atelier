package com.rithsagea.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.User;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicQueueCommand extends MusicSubCommand {

	public MusicQueueCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}
	
	@Override
	public String getLabel() {
		return "queue";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("q");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}

	@Override
	public void execute(User user, AtelierAudioHandler audioHandler,  List<String> args, MessageReceivedEvent event) {
		List<AudioTrack> tracks = audioHandler.getQueue();
		
		//TODO create pagination here, use action buttons
		// possibly add external persistent holder for
		// message action information
	}

}
