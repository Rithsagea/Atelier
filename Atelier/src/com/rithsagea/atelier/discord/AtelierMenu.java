package com.rithsagea.atelier.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public abstract class AtelierMenu {
	
	private MessageChannel channel;
	
	public AtelierMenu(MessageChannel channel) {
		this.channel = channel;
		
		channel.sendMessage(getMessage());
	}
	
	public abstract Message getMessage();
	public abstract void onButtonClick(ButtonInteractionEvent event);
	public abstract void onMenuSelect(SelectMenuInteractionEvent event);
}
