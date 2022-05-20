package com.tempera.atelier.discord.music;

import java.awt.Color;

import com.tempera.atelier.discord.MenuManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class QueueMessageBuilder extends MessageBuilder {

	private int page;
	private MenuManager m;
	private AtelierAudioHandler handler;

	public QueueMessageBuilder(AtelierAudioHandler audioHandler, int pageNum, Message message,  MenuManager m) {
		this.page = pageNum;
		this.handler = audioHandler;
		this.m = m;
		this.calcQueue();
	}

	public void calcQueue() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.CYAN);
		StringBuilder msg = new StringBuilder();
		int queueSize = handler.getQueue().size();
		int pageMax = (int)Math.ceil((double)queueSize / 10);

		page = Math.min(page, pageMax);

		if (queueSize > 0) {
			for (int i = (page-1)*10; i < queueSize && i < page*10; i++) {
				msg.append(String.format("`[%d]` - **%s**\n",
						i + 1, 
						handler.getQueue().get(i).getInfo().title));
			}
			eb.setTitle(queueSize + (queueSize > 1 ?" songs" :" song"));
			eb.addField("Queue:", msg.toString(), true);
			eb.appendDescription(String.format("Page %d of %d", page, pageMax));
			eb.setFooter(String.format("Displaying songs %d to %d out of %d", (page-1)*10+1,
					Math.min(page*10, queueSize), queueSize));
		}
		else {
			eb.setTitle("No songs in queue!");
			this.setActionRows(ActionRow.of(Button.success("asdf", "bad time")));
		}
		this.setEmbeds(eb.build());
	}

	public void setPage(int num) {
		page = num;
	}

	public int getPage() {
		return page;
	}

	public void send(MessageChannel channel) {
		if (handler.getQueue().size() > 10) {
			channel.sendMessage(this.build()).queue((Message message) -> {
				m.addMenu(message.getIdLong(), new QButton(message, this));
			});
		}
		else
			channel.sendMessage(this.build()).queue();
	}
	
}