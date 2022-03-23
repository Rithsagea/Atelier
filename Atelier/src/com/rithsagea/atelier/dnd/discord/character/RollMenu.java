package com.rithsagea.atelier.dnd.discord.character;

import com.rithsagea.atelier.discord.menu.AtelierMenu;
import com.rithsagea.atelier.discord.menu.AtelierMenuManager;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class RollMenu extends AtelierMenu {
	public RollMenu(AtelierMenuManager menuManager, MessageAction msg) {
		super(menuManager);
		
		init(msg.content("Select value to roll for")
			.setActionRows(
					ActionRow.of(
							Button.of(ButtonStyle.PRIMARY, "d4", "D4"),
							Button.of(ButtonStyle.PRIMARY, "d6", "D6"),
							Button.of(ButtonStyle.PRIMARY, "d10", "D10"),
							Button.of(ButtonStyle.PRIMARY, "d12", "D12"),
							Button.of(ButtonStyle.PRIMARY, "d20", "D20"))));
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		
		int val;
		
		switch(event.getButton().getId()) {
			case "d4": val = 4; break;
			case "d6": val = 6; break;
			case "d10": val = 10; break;
			case "d12": val = 12; break;
			case "d20": val = 20; break;
			default: val = 0; System.out.println(event.getId());
		}
		
		event.reply("Rolled a " + (int) (Math.random() * val + 1)).queue();
	}

	@Override
	public void onMenuSelect(SelectMenuInteractionEvent event) {
		// TODO Auto-generated method stub
		
	}
}
