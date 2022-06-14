package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LoopCommand extends MusicSubCommand {

	public LoopCommand(AtelierAudioManager audioManager) {
		super(audioManager, "loop", "Turns loop on and off");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		event.reply(
			audioHandler.toggleLoop() ? "Loop enabled." : "Loop disabled.")
		.queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
