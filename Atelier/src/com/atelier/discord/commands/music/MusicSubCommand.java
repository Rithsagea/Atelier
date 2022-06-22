package com.atelier.discord.commands.music;

import com.atelier.discord.User;
import com.atelier.discord.commands.BaseSubcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class MusicSubCommand extends BaseSubcommand {
	private AtelierAudioManager audioManager = AtelierAudioManager.getInstance();
	
	public MusicSubCommand(String name, String description) {
		super(name, description);
	}
	
	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		execute(audioManager.getAudioHandler(event.getGuild(), event.getChannel()), user, event);
	}

	public abstract void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event);

}
