package com.atelier.discord.commands.music;

import com.atelier.discord.AtelierUser;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicSansCommand extends MusicSubCommand {

	@Override
	public void execute(AtelierAudioHandler audioHandler, AtelierUser user, SlashCommandInteractionEvent event) {
		audioHandler.loadTrack("https://www.youtube.com/watch?v=wDgQdr8ZkTw");
	}
	
}
