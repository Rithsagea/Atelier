package com.atelier.discord.commands.music;

import java.awt.Color;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.atelier.discord.AtelierUser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicNowPlayingCommand extends MusicSubCommand {

	@Override
	public void execute(AtelierAudioHandler audioHandler, AtelierUser user, SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.WHITE);
		if (audioHandler.getPlayingTrack() != null) {
			eb.addField("Now playing:", audioHandler.getPlayingTrack().getInfo().title, true);

			LocalTime cur = LocalTime.MIN.plusSeconds(audioHandler.getPlayingTrack().getPosition() / 1000);;
			LocalTime dur = LocalTime.MIN.plusSeconds(audioHandler.getPlayingTrack().getInfo().length / 1000);
			
			if (dur.getHour() > 0) {
				eb.setFooter(String.format("%s out of %s",
					cur.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
					dur.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
			} else {
				eb.setFooter(String.format("%s out of %s",
					cur.format(DateTimeFormatter.ofPattern("mm:ss")),
					dur.format(DateTimeFormatter.ofPattern("mm:ss"))));
			}
			String url = audioHandler.getPlayingTrack().getInfo().uri;
			eb.setImage(String.format("https://img.youtube.com/vi/%s/sddefault.jpg", 
					url.substring(url.indexOf("v=") + 2)));
		} else {
			eb.setTitle("No songs playing!");
		}

		event.replyEmbeds(eb.build()).queue();
	}
}
