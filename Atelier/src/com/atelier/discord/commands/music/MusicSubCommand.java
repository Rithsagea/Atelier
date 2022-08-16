package com.atelier.discord.commands.music;

import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.BaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class MusicSubCommand extends BaseSubcommand {
	
	private AtelierAudioManager audioManager = AtelierAudioManager.getInstance();
	
	@Override
	public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
		execute(audioManager.getAudioHandler(event.getGuild(), event.getChannel()), user, event);
	}

	public abstract void execute(AtelierAudioHandler audioHandler, AtelierUser user, SlashCommandInteractionEvent event);

}
