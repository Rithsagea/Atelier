package com.tempera.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class EmbedUtil {
	public static MessageEmbed getEmbed(String url, String title) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(title);
		eb.setImage(url);

		return eb.build();
	}
}
