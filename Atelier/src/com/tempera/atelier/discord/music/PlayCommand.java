package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayCommand extends MusicSubCommand {

	public PlayCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "play";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user,
		List<String> args, MessageReceivedEvent event) {
		if (args.size() < 2)
			event.getChannel()
				.sendMessage("Missing URL!")
				.queue();
		else {
			if (audioHandler.joinVc(event) == null)
				return;
			audioHandler.loadTrack(args.get(1));
		}
	}

}
