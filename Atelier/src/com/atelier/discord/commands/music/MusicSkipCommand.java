package com.atelier.discord.commands.music;

import java.util.Collections;

import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class MusicSkipCommand extends MusicSubCommand {
	
	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		if (audioHandler.getPlayingTrack() != null) {
			if (event.getOption("tracks") == null) {
				event.reply(String.format("Skipping `%s`!", audioHandler.getPlayingTrack().getInfo().title)).queue();
				audioHandler.nextTrack();
			}
			else {
				event.reply(String.format("Skipping %d tracks!", event.getOption("tracks").getAsInt())).queue();
				Collections.nCopies(event.getOption("tracks").getAsInt(), 1)
					.forEach(x -> audioHandler.nextTrack());
			}
		} else {
			event.reply("No song to skip!").queue();
		}
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, "tracks", "The amount of tracks to skip", false, false);
	}
}
