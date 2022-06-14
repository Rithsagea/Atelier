package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class TogglePauseCommand extends MusicSubCommand{

	public TogglePauseCommand(AtelierAudioManager audioManager) {
		super(audioManager, "pause", "Stops or stops playing tracks temporarily");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
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
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
