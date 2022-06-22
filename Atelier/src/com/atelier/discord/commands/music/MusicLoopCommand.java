package com.atelier.discord.commands.music;

import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicLoopCommand extends MusicSubCommand {

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		event.reply(audioHandler.toggleLoop() ? "Loop enabled." : "Loop disabled.").queue();
	}

}
