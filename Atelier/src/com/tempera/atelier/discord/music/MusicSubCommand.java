package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.SlashBaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class MusicSubCommand extends SlashBaseSubcommand {
	private AtelierAudioManager audioManager;
	
	public MusicSubCommand(AtelierAudioManager audioManager, String name, String description) {
		super(name, description);
		this.audioManager = audioManager;
	}
	
	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		execute(audioManager.getAudioHandler(event.getGuild(), event.getChannel()), user, event);
	}

	public abstract void execute(AtelierAudioHandler audioHandler, User user
			,SlashCommandInteractionEvent event);

}
