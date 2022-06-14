package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SansListCommand extends MusicSubCommand{

	public SansListCommand(AtelierAudioManager audioManager) {
		super(audioManager, "sanss", "badd timee");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		audioHandler.loadTrack("https://www.youtube.com/playlist?list=PLTmmZZ44iAEeZPba3BYs4OR8BmSDQQhl7");
		event.getChannel()
		.sendMessage("birds are singing. flowers are blooming. on days like these kids like you...... should be burning in heck")
		.queue();
	}
	
	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
