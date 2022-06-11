package com.tempera.atelier.discord;

import java.util.UUID;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public interface SlashMenu {
	public Message initialize();
	public UUID getId();

	public void onButtonInteract(ButtonInteractionEvent event);
	public void onSelectInteract(SelectMenuInteractionEvent event);
}
