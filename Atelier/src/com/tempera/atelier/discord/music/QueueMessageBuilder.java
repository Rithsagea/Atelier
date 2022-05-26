package com.tempera.atelier.discord.music;

import java.awt.Color;

import com.tempera.atelier.discord.MenuManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class QueueMessageBuilder extends MessageBuilder {
	public QueueMessageBuilder(AtelierAudioHandler audioHandler, MenuManager menuManager, int page) {
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.CYAN);
		StringBuilder msg = new StringBuilder();
		int queueSize = audioHandler.getQueue().size();
		int pageMax = (int) Math.ceil(queueSize / 10d);

		page = Math.min(page, pageMax);

		if (queueSize > 0) {
			for (int i = (page - 1) * 10; i < queueSize && i < page * 10; i++) {
				msg.append(String.format("`[%d]` - **%s**\n",
						i + 1, 
						audioHandler.getQueue().get(i).getInfo().title));
			}
			
			eb.setTitle(queueSize + (queueSize > 1 ?" songs" :" song"));
			eb.addField("Queue:", msg.toString(), true);
			eb.appendDescription(String.format("Page %d of %d", page, pageMax));
			eb.setFooter(String.format("Displaying songs %d to %d out of %d", (page-1)*10+1,
					Math.min(page*10, queueSize), queueSize));
			setActionRows(ActionRow.of(
					Button.primary("prev", "Previous"),
					Button.primary("next", "Next")));
		}
		else {
			eb.setTitle("No songs in queue!");
			setActionRows(ActionRow.of(
					Button.success("ha", "bad time")));
		}
		
		setEmbeds(eb.build());
	}
}