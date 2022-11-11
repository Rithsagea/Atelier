package com.atelier.discord.commands.dnd;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.GroupCommand;
import com.atelier.discord.commands.dnd.embeds.AttributeListEmbedBuilder;
import com.atelier.dnd.character.AtelierCharacter;
import com.atelier.dnd.character.CharacterAttribute;
import com.rithsagea.util.data.DataUtil;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class AttributeCommand extends GroupCommand {
	private class AttributeList extends BaseSubcommand {
		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			MessageEmbed embed = new AttributeListEmbedBuilder(user.getSelectedCharacter()).build();
			event.replyEmbeds(embed).setEphemeral(true).queue();
		}
	}

	private class AttributeInfo extends BaseSubcommand {

		private String category = getProperty("category.name");
		private String categoryDescription = getProperty("category.description");
		private String attribute = getProperty("attribute.name");
		private String attributeDescription = getProperty("attribute.description");

		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			AtelierCharacter character = user.getSelectedCharacter();
			CharacterAttribute characterAttribute = null;

			String label = event.getOption(attribute).getAsString();
			switch(event.getOption(category).getAsString()) {
				case "class": characterAttribute = character.getCharacterClass().getFeatures().get(label); break;
				case "race": characterAttribute = character.getCharacterRace().getTraits().get(label); break;
				default: event.reply(getError("category").get()).setEphemeral(true).queue(); return;
			}

			event.reply(characterAttribute.toString()).setEphemeral(true).queue();
		}

		@Override
		public void addOptions(SubcommandData data) {
			data.addOption(OptionType.STRING, category, categoryDescription, true, true);
			data.addOption(OptionType.STRING, attribute, attributeDescription, true, true);
		}

		@Override
		public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
			AtelierCharacter character = user.getSelectedCharacter();

			if(event.getFocusedOption().getName().equals(category)) {
				Stream<Command.Choice> choices = Stream.of(
					new Command.Choice(character.getCharacterClass().toString(), "class"),
					new Command.Choice(character.getCharacterRace().toString(), "race"));
				event.replyChoices(choices
					.filter(c -> c.getName().startsWith(event.getFocusedOption().getValue()))
					.collect(Collectors.toList())).queue();
			}

			if(event.getFocusedOption().getName().equals(attribute)) {
				if(event.getOption(category) == null) {
					event.replyChoices().queue();
					return;
				}
				String selectedCategory = event.getOption(category).getAsString();

				List<Stream<Entry<String, CharacterAttribute>>> attributes;

				Stream<Entry<String, CharacterAttribute>> features = character.getCharacterClass().getFeatures().entrySet().stream()
					.map(e -> new SimpleEntry<String, CharacterAttribute>(e.getKey(), (CharacterAttribute)e.getValue()));
				Stream<Entry<String, CharacterAttribute>> traits = character.getCharacterRace().getTraits().entrySet().stream()
					.map(e -> new SimpleEntry<String, CharacterAttribute>(e.getKey(), (CharacterAttribute)e.getValue()));;

				if(selectedCategory.equals("class"))
					attributes = DataUtil.list(features); //TODO figure out how to get this to stop complaining
				else if(selectedCategory.equals("race"))
					attributes = DataUtil.list(traits);
				else attributes = Collections.emptyList();

				event.replyChoices(attributes.stream()
					.flatMap(i -> i)
					.filter(e -> e.getValue().toString().startsWith(event.getFocusedOption().getValue()))
				  .map(e -> new Command.Choice(e.getValue().toString(), e.getKey()))
				  .collect(Collectors.toList())).queue();
			}
		}
	}
 
	public AttributeCommand() {
		registerSubcommand(new AttributeList());
		registerSubcommand(new AttributeInfo());
	}
}
