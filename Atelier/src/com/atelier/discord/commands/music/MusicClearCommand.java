package com.atelier.discord.commands.music;

import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicClearCommand extends MusicSubCommand {

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		event.reply("Clearing queue!").queue();
		audioHandler.clearQueue();
	}
}
