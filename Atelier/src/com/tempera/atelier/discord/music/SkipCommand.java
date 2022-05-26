package com.tempera.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SkipCommand extends MusicSubCommand{

	public SkipCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "skip";
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
		if (audioHandler.getPlayingTrack() != null) {
			event.getChannel().sendMessage(String.format("Skipping `%s`!", audioHandler.getPlayingTrack().getInfo().title)).queue();
			audioHandler.nextTrack();
		}
		else
			event.getChannel().sendMessage("No song to skip!").queue();
	}

}
