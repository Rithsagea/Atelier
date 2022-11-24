package com.atelier.discord.commands.dnd;

import java.util.UUID;
import java.util.stream.Collectors;

import com.atelier.database.AtelierDB;
import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.GroupCommand;
import com.atelier.discord.commands.dnd.embeds.CharacterInfoEmbedBuilder;
import com.atelier.discord.commands.dnd.embeds.CharacterListEmbedBuilder;
import com.atelier.dnd.character.AtelierCharacter;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CharacterCommand extends GroupCommand {
	private class CharacterList extends BaseSubcommand {
		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			event.replyEmbeds(new CharacterListEmbedBuilder(user).build())
				.setEphemeral(true).queue();
		}
	}
	
	private class CharacterSelect extends BaseSubcommand { 
		
		private String id = getProperty("id.name");
		private String idDescription = getProperty("id.description");
		
		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			UUID characterId = UUID.fromString(event.getOption(id).getAsString());
			AtelierCharacter character = AtelierDB.getInstance().getCharacter(characterId);
			
			try {
				user.selectedCharacter(character);
				event.reply("Selected Character: " + character).setEphemeral(true).queue();
			} catch(Exception e) {
				event.reply("Error").setEphemeral(true).queue();
			}
		}
		
		@Override
		public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
			if(event.getFocusedOption().getName().equals(id)) {
				event.replyChoices(user.getCharacters().stream()
					.filter(c -> c.getName().startsWith(event.getFocusedOption().getValue()))
					.map(c -> new Command.Choice(c.getName(), c.getId().toString()))
					.collect(Collectors.toList())).queue();
			}
		}
		
		@Override
		public void addOptions(SubcommandData data) {
			data.addOption(OptionType.STRING, id, idDescription, true, true);
		}
	}
	
	private class CharacterInfo extends BaseSubcommand {
		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			event.replyEmbeds(new CharacterInfoEmbedBuilder(user.getSelectedCharacter()).build()).setEphemeral(true).queue();
		}
	}

	public CharacterCommand() {
		registerSubcommand(new CharacterList());
		registerSubcommand(new CharacterSelect());
		registerSubcommand(new CharacterInfo());
	}
}
