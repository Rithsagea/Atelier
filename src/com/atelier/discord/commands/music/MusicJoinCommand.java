package com.atelier.discord.commands.music;

import com.atelier.discord.AtelierUser;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicJoinCommand extends MusicSubCommand {

	@Override
	public void execute(AtelierAudioHandler audioHandler, AtelierUser user, SlashCommandInteractionEvent event) {
		event.deferReply();
		audioHandler.join(event);
	}
}
