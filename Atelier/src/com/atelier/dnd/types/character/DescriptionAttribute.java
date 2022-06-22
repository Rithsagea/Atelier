package com.atelier.dnd.types.character;

import com.atelier.discord.Menu;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class DescriptionAttribute implements CharacterAttribute {
	
	private String name;
	private String description;
	
	public DescriptionAttribute(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public Menu getMenu() {
		return new DescriptionMenu();
	}

	private class DescriptionMenu implements Menu {
		@Override
		public Message initialize() {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle(getName());
			eb.setDescription(getDescription());
			
			return new MessageBuilder(eb.build()).build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) { }

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) { }
		
	}
}
