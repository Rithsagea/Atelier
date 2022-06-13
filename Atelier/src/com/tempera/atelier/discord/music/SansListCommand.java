package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SansListCommand extends MusicSubCommand{

	public SansListCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	@Override
	public String getLabel() {
		return "sanss";
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
		audioHandler.loadTrack("https://www.youtube.com/playlist?list=PLTmmZZ44iAEeZPba3BYs4OR8BmSDQQhl7");
		event.getChannel()
		.sendMessage("birds are singing. flowers are blooming. on days like these kids like you...... should be burning in heck")
		.queue();
	}

}
