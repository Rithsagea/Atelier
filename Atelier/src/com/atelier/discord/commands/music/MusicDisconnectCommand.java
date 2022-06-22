package com.atelier.discord.commands.music;

import com.atelier.discord.User;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class MusicDisconnectCommand extends MusicSubCommand {

	public MusicDisconnectCommand() {
		super("disconnect", "Disconnects from voice channel");
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, SlashCommandInteractionEvent event) {
		AudioManager manager = event.getGuild().getAudioManager();
		GuildVoiceState state = event.getMember().getVoiceState();

		if (!manager.isConnected()) {
			event.reply("Not in a voice channel!").queue();
			return;
		}
		
		audioHandler.clearQueue();
		audioHandler.nextTrack();
		manager.closeAudioConnection();
		
		event.reply(String.format("Disconnected from %s", state.getChannel().getAsMention())).queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {}

}
