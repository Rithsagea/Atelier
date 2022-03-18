package com.rithsagea.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.discord.PermissionLevel;
import com.rithsagea.atelier.dnd.User;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicSkipCommand extends MusicSubCommand {

	public MusicSkipCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "skip";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("s");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}

	@Override
	public void execute(User user, AtelierAudioHandler audioHandler, List<String> args, MessageReceivedEvent event) {
		AudioTrack track = audioHandler.getPlayingTrack();
		audioHandler.nextTrack();
		
		event.getChannel().sendMessageFormat("Skipped Track %s", track.getInfo().title).queue();
	}
	
}
