package com.atelier.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public interface Menu {
	
	public Message initialize();
	public void onButtonInteract(ButtonInteractionEvent event);
	public void onSelectInteract(SelectMenuInteractionEvent event);
}
