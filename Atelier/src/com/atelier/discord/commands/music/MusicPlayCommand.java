package com.atelier.discord.commands.music;

import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class MusicPlayCommand extends MusicSubCommand {
	
	public MusicPlayCommand() {
		super("play", "Adds a song or playlist to queue");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		String url = event.getOption("url").getAsString();
		event.reply("Loading url: " + url).queue();
		
		if (!event.getGuild().getAudioManager().isConnected())
			if (audioHandler.join(event) == null) return;
		
		audioHandler.loadTrack(url);
	}

	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, "url", "The song URL to play", true, false);
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
