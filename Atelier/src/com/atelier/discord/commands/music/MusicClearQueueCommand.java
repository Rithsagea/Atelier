package com.atelier.discord.commands.music;

import com.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicClearQueueCommand extends MusicSubCommand{

	public MusicClearQueueCommand() {
		super("clear", "Clears the queue");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		event.reply("Clearing queue!").queue();
		audioHandler.clearQueue();
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
