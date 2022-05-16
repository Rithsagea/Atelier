package com.tempera.atelier.discord.music;

import com.tempera.atelier.discord.Menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class QButton extends Menu{
	
	private Message message;
	private int page;
	
	public QButton(Message message, int page) {
		super(message);
		message.editMessageComponents(ActionRow.of(Button.primary("prev", "Previous"), Button.primary("next", "Next"))).queue();
		this.page = page;
	}
	
	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		 if (event.getComponentId().equals("prev")) {
			System.out.println("as");
		 }
		 else if (event.getComponentId().equals("next")) {
			System.out.println("df");
		 }
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {
		// TODO Auto-generated method stub
		
	}
}
