package com.tempera.atelier.discord.commands;

import java.awt.Color;

import com.tempera.atelier.discord.Menu;
import com.tempera.util.NekoUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

public class WaifuMenu implements Menu {
	
	private String selectedWaifu = "waifu";
	
	@Override
	public void initialize(Message message) {
		SelectMenu.Builder b = SelectMenu.create("waifuMenu");
		b.setPlaceholder("Waifu Type");
		b.addOption("Cat", "cat");
		b.addOption("Dog", "dog");
		b.addOption("Waifu", "waifu");
		b.addOption("Catboy", "catboy");
		b.addOption("Catgirl", "catgirl");
		b.addOption("Foxgirl", "foxgirl");
		b.setMaxValues(1);
		
		message.editMessageComponents(
				ActionRow.of(b.build()),
				ActionRow.of(Button.primary("get", "Get")))
			.queue();
	}
	
	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		String url;
		
		switch(selectedWaifu) {
			case "cat": url = NekoUtil.getCat(); break;
			case "dog": url = NekoUtil.getDog(); break;
			case "catboy": url = NekoUtil.getCatboy(); break;
			case "catgirl": url = NekoUtil.getCatgirl(); break;
			case "foxgirl": url = NekoUtil.getFoxgirl(); break;
			default: url = NekoUtil.getWaifu(); break;
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.CYAN);
		eb.setImage(url);
		
		event.replyEmbeds(eb.build()).queue();
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {
		selectedWaifu = event.getSelectedOptions().get(0).getValue();
		event.deferEdit().queue();
	}

}
