package com.tempera.atelier.discord.music;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayingCommand extends MusicSubCommand{

	public PlayingCommand(AtelierAudioManager audioManager) {
		super(audioManager);
	}

	public String getLabel() {
		return "playing";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("m", "audio", "np");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, List<String> args, MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.WHITE);
		if (audioHandler.getPlayingTrack() != null) {
		eb.addField("Now playing:", audioHandler.getPlayingTrack().getInfo().title, true);
		eb.setFooter(String.format("%s:%s",Integer.toString((int)audioHandler.getPlayingTrack().getDuration()), 
				Integer.toString((int)audioHandler.getPlayingTrack().getInfo().length)));
		}
		else {
			eb.setTitle("No songs playing!");
		}
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
}
