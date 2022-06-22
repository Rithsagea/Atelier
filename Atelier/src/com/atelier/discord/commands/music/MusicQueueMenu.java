package com.atelier.discord.commands.music;

import com.atelier.discord.Menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class MusicQueueMenu implements Menu {

	private AtelierAudioHandler audioHandler;
	private int page;

	public MusicQueueMenu(AtelierAudioHandler audioHandler, int page) {
		this.audioHandler = audioHandler;
		this.page = page;
	}

	@Override
	public Message initialize() {
		return new MusicQueueMessageBuilder(audioHandler, page).build();
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		switch (event.getComponentId()) {
		case "prev":
			page = Math.max(page - 1, 0);
			break;
		case "next":
			page = Math.min(page + 1, (int) Math.ceil(audioHandler.getQueue().size() / 10d));
			break;
		default:
			return;
		}

		event.editMessage(new MusicQueueMessageBuilder(audioHandler, page).build()).queue();
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) { }
}
