package com.atelier.discord.commands.music;

import com.atelier.discord.AtelierUser;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class MusicPauseCommand extends MusicSubCommand{

	@Override
	public void execute(AtelierAudioHandler audioHandler, AtelierUser user, SlashCommandInteractionEvent event) {
		if (!event.getOption("paused").getAsBoolean()) {
			event.reply("Resuming").queue();
			audioHandler.setPausedState(false);
			return;
		}
		event.reply("Pausing").queue();
		audioHandler.setPausedState(true);
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.BOOLEAN, "paused", "The pause state", true, false);
	}
}
