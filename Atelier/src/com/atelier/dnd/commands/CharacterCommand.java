package com.atelier.dnd.commands;

import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.GroupCommand;
import com.atelier.dnd.AtelierCharacter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CharacterCommand extends GroupCommand {
	private class CharacterList extends BaseSubcommand {
		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle(getMessage("title").addUser(user).get());
			for(AtelierCharacter character : user.getCharacters()) {
				eb.appendDescription(character + "\n");
			}
			
			event.replyEmbeds(eb.build()).queue();
		}
	}
	
	public CharacterCommand() {
		registerSubcommand(new CharacterList());
	}
}
