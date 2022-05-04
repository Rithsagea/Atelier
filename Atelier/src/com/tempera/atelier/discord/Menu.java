package com.tempera.atelier.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public abstract class Menu {
	
	private Message message;
	
	public Menu(Message message) {
		this.message = message;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public abstract void onButtonInteract(ButtonInteractionEvent event);
	public abstract void onSelectInteract(SelectMenuInteractionEvent event);
}
