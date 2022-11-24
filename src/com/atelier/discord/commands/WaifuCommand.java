package com.atelier.discord.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.BaseCommand;
import com.atelier.util.NekoUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class WaifuCommand extends BaseCommand {

	private class WaifuType {
		public String name;
		public String title;
		public Supplier<String> supplier;
	}
	
	private Map<String, WaifuType> typeMap;
	private WaifuType defaultType;
	private String[] choices;
	private String typeName = getProperty("type.name");
	private String typeText = getProperty("type.text");
	
	private void addType(String id, Supplier<String> supplier) {
		WaifuType type = new WaifuType();
		
		type.name = getProperty(id + ".name");
		type.title = getProperty(id + ".title");
		type.supplier = supplier;
		
		typeMap.put(type.name, type);
	}
	
	public WaifuCommand() {
		typeMap = new HashMap<>();
		addType("catgirl", NekoUtil::getCatgirl);
		addType("foxgirl", NekoUtil::getFoxgirl);
		addType("catboy", NekoUtil::getCatboy);
		addType("cat", NekoUtil::getCat);
		addType("dog", NekoUtil::getDog);
		
		choices = typeMap.keySet().toArray(new String[typeMap.size()]);
		
		defaultType = new WaifuType();
		defaultType.name = getProperty("waifu.name");
		defaultType.title = getProperty("waifu.title");
		defaultType.supplier = NekoUtil::getWaifu;
	}
	
	@Override
	public void addOptions(SlashCommandData data) {
		data.addOption(OptionType.STRING, typeName, typeText, false, true);
	}
	
	@Override
	public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		WaifuType type = null;
		if(event.getOption(typeName) != null) 
			type = typeMap.get(event.getOption(typeName).getAsString());
		if(type == null) type = defaultType;
		
		eb.setTitle(type.title);
		eb.setImage(type.supplier.get());
		
		event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}

	@Override
	public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
		event.replyChoiceStrings(choices).queue();
	}

}
