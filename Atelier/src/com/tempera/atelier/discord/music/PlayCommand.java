package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class PlayCommand extends MusicSubCommand {
	
	public PlayCommand(AtelierAudioManager audioManager) {
		super(audioManager, "play", "Adds a song or playlist to queue");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		if (!event.getGuild().getAudioManager().isConnected())
			if (audioHandler.joinVc(event) == null)
				return;
			audioHandler.loadTrack(event.getOption("url").getAsString());
	}

	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, "url", "The song URL to play", true, false);
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
