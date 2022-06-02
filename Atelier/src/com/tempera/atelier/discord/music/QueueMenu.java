package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.discord.MenuManager;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class QueueMenu implements Menu {

	private AtelierAudioHandler audioHandler;
	private MenuManager menuManager;
	private int page;
	
	public QueueMenu(AtelierAudioHandler audioHandler, MenuManager menuManager, int page) {
		this.audioHandler = audioHandler;
		this.menuManager = menuManager;
		this.page = page;
	}
	
	@Override
	public Message initialize() {
		return new QueueMessageBuilder(audioHandler, menuManager, page).build();
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		switch(event.getComponentId()) {
			case "prev":
				page = Math.max(page - 1, 0);
				break;
			case "next":
				page = Math.min(page + 1, (int) Math.ceil(audioHandler.getQueue().size() / 10d));
				break;
			default:
				return;
		}
		
		event.editMessage(new QueueMessageBuilder(audioHandler, menuManager, page).build()).queue();
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) { }
}
