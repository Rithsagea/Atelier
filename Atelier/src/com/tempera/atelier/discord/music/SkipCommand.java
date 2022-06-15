package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SkipCommand extends MusicSubCommand {

	public SkipCommand(AtelierAudioManager audioManager) {
		super(audioManager, "skip", "Skips the currently playing track");
	}
	
	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		if (audioHandler.getPlayingTrack() != null) {
			if (event.getOption("tracks") == null) {
			event.reply(String.format("Skipping `%s`!",
					audioHandler.getPlayingTrack()
						.getInfo().title))
				.queue();
			audioHandler.nextTrack();
			}
			else {
				event.reply(String.format("Skipping %d tracks!", event.getOption("tracks").getAsInt())).queue();
				for (int i = 0; i < event.getOption("tracks").getAsInt(); i++)
					audioHandler.nextTrack();
			}
		} else
			event.reply("No song to skip!")
				.queue();
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, "tracks", "The amount of tracks to skip", false, false);
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}
}
