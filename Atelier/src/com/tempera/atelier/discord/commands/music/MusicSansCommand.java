package com.tempera.atelier.discord.commands.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicSansCommand extends MusicSubCommand {

	public MusicSansCommand() {
		super("sans", "bad time");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		audioHandler.loadTrack("https://www.youtube.com/watch?v=wDgQdr8ZkTw");
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}
}
