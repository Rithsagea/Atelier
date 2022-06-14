package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SkipCommand extends MusicSubCommand {

	public SkipCommand(AtelierAudioManager audioManager) {
		super(audioManager, "skip", "Skips the currently playing track");
	}
	
	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		if (audioHandler.getPlayingTrack() != null) {
			event.getChannel()
				.sendMessage(String.format("Skipping `%s`!",
					audioHandler.getPlayingTrack()
						.getInfo().title))
				.queue();
			audioHandler.nextTrack();
		} else
			event.getChannel()
				.sendMessage("No song to skip!")
				.queue();
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}
}
