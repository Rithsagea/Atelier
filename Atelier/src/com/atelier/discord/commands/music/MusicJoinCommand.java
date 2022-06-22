package com.atelier.discord.commands.music;

import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicJoinCommand extends MusicSubCommand {

	public MusicJoinCommand() {
		super("join", "Joins the user's current voice channel");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		event.deferReply();
		audioHandler.join(event);
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
