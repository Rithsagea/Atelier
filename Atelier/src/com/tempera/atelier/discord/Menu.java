package com.tempera.atelier.discord;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public abstract class Menu {
	
	public abstract MessageAction initialize(MessageChannel channel);
	
	public abstract void onButtonInteract(ButtonInteractionEvent event);
	public abstract void onSelectInteract(SelectMenuInteractionEvent event);
}
