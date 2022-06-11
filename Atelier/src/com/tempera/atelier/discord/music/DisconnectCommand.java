package com.tempera.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.PermissionLevel;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class DisconnectCommand extends MusicSubCommand {

	public DisconnectCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "disconnect";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("dc");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user,
		List<String> args, MessageReceivedEvent event) {
		if (event.getAuthor()
			.isBot())
			return;

		AudioManager manager = event.getGuild()
			.getAudioManager();
		GuildVoiceState state = event.getMember()
			.getVoiceState();

		if (!manager.isConnected()) {
			event.getChannel()
				.sendMessage("Not in a voice channel!")
				.queue();
			return;
		}
		manager.closeAudioConnection();
		event.getChannel()
			.sendMessage(String.format("Disconnected from channel `[%s]`",
				state.getChannel()
					.getName()))
			.queue();
	}

}
