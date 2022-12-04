package com.atelier.dnd.character.attributes;

import com.atelier.discord.Menu;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class DescriptionMenu implements Menu {

	private CharacterAttribute attribute;

	public DescriptionMenu(CharacterAttribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public Message initialize() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(attribute.getProperty("name"));
		embed.setDescription(attribute.getProperty("description"));
		return new MessageBuilder().setEmbeds(embed.build()).build();
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) { event.deferEdit().queue(); }

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) { event.deferEdit().queue(); }
}
