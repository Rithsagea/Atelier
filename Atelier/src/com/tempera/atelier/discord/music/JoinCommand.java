package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class JoinCommand extends MusicSubCommand {

	public JoinCommand(AtelierAudioManager audioManager) {
		super(audioManager, "join", "Joins the user's current voice channel");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		audioHandler.joinVc(event);
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
