package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SansCommand extends MusicSubCommand{

	public SansCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "sans";
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
	public void execute(AtelierAudioHandler audioHandler, User user, List<String> args, MessageReceivedEvent event) {
		int times = 1;
		if (args.size() > 1)
			times = Integer.parseInt(args.get(1));
		for (int i = 0 ; i < times; i++) {
			audioHandler.loadTrack("https://www.youtube.com/watch?v=wDgQdr8ZkTw");
			event.getChannel().sendMessage("bad time").queue();
		}
	}
}
