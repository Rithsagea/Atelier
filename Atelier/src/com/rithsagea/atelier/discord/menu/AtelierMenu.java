package com.rithsagea.atelier.discord.menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public abstract class AtelierMenu {
	private Message msg;
	private AtelierMenuManager menuManager;
	
	public AtelierMenu(AtelierMenuManager menuManager) {
		this.menuManager = menuManager;
	}
	
	protected void init(MessageAction msg) {
		msg.queue((Message m) -> {
			setMessage(m);
			menuManager.registerMenu(this);
		});
	}
	
	private void setMessage(Message msg) {
		this.msg = msg;
	}
	
	public Message getMessage() {
		return msg;
	}
	
	public abstract void onButtonInteract(ButtonInteractionEvent event);
	public abstract void onMenuSelect(SelectMenuInteractionEvent event);
}
