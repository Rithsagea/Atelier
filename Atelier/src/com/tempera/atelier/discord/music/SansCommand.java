package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SansCommand extends MusicSubCommand {

	public SansCommand(AtelierAudioManager audioManager) {
		super(audioManager, "sans", "bad time");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		int times = 1;
		if (event.getOption("time") != null)
			times = (int) event.getOption("time").getAsLong();
		for (int i = 0; i < times; i++) {
			audioHandler
				.loadTrack("https://www.youtube.com/watch?v=wDgQdr8ZkTw");
			event.reply("bad time")
				.queue();
		}
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.INTEGER, "time", "The time", false, false);
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}
}
