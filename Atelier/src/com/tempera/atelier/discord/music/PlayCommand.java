package com.tempera.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayCommand extends MusicSubCommand{

	public PlayCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "play";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("m", "audio");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}
	
	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, List<String> args, MessageReceivedEvent event) {
		audioHandler.loadTrack(args.get(1));
		event.getChannel();
	
	}
	
}
