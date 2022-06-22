package com.atelier.discord.commands.music;

import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicSansCommand extends MusicSubCommand {

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		audioHandler.loadTrack("https://www.youtube.com/watch?v=wDgQdr8ZkTw");
	}
	
}
