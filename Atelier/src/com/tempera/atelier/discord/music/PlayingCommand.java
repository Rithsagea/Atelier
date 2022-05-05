package com.tempera.atelier.discord.music;

import java.awt.Color;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

			LocalTime cur = LocalTime.MIN;
			LocalTime dur = LocalTime.MIN;
			cur = cur.plusSeconds(audioHandler.getPlayingTrack().getPosition()/1000);
			dur = dur.plusSeconds(audioHandler.getPlayingTrack().getInfo().length/1000);
			if (dur.getHour() > 0) {
				eb.setFooter(String.format("%s out of %s",cur.format(DateTimeFormatter.ofPattern("HH:mm:ss")), 
						dur.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
			}
			else {
				eb.setFooter(String.format("%s out of %s",cur.format(DateTimeFormatter.ofPattern("mm:ss")), 
						dur.format(DateTimeFormatter.ofPattern("mm:ss"))));
			}
		}
		else {
			eb.setTitle("No songs playing!");
		}

		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
}
