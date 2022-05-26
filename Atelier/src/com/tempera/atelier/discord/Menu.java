package com.tempera.atelier.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public abstract class Menu {
	
	public abstract Message initialize();
	
	public abstract void onButtonInteract(ButtonInteractionEvent event);
	public abstract void onSelectInteract(SelectMenuInteractionEvent event);
}
