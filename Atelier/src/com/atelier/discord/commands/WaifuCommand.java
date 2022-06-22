package com.atelier.discord.commands;

import com.atelier.discord.User;
import com.atelier.discord.commands.BaseInteraction.BaseCommand;
import com.atelier.util.NekoUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class WaifuCommand extends BaseCommand {

	@Override
	public void addOptions(SlashCommandData data) {
		data.addOption(OptionType.STRING, "type", "the type of waifu to get", false, true);
	}
	
	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("Waifu");
		if(event.getOption("type") != null) {
			switch(event.getOption("type").getAsString()) {
				case "catgirl":
					eb.setImage(NekoUtil.getCatgirl());
					eb.setTitle("Catgirl");
					break;
				case "foxgirl":
					eb.setImage(NekoUtil.getFoxgirl());
					eb.setTitle("Foxgirl");
					break;
				case "catboy":
					eb.setImage(NekoUtil.getCatboy());
					eb.setTitle("Catboy");
					break;
				case "cat":
					eb.setImage(NekoUtil.getCat());
					eb.setTitle("Cat");
					break;
				case "dog":
					eb.setImage(NekoUtil.getDog());
					eb.setTitle("Dog");
					break;
				default: 
					eb.setImage(NekoUtil.getWaifu()); break;
			}
		} else {
			eb.setImage(NekoUtil.getWaifu());
		}
		
		event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		event.replyChoiceStrings("catgirl", "foxgirl", "catboy", "cat", "dog").queue();
	}

}
