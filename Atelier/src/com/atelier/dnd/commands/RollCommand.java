package com.atelier.dnd.commands;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.atelier.discord.AtelierUser;
import com.atelier.discord.commands.BaseInteraction.GroupCommand;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.character.AtelierCharacter;
import com.atelier.dnd.embeds.RollEmbedBuilder.RollSavingEmbedBuilder;
import com.atelier.dnd.embeds.RollEmbedBuilder.RollSkillEmbedBuilder;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class RollCommand extends GroupCommand {
	private class RollSaving extends BaseSubcommand {

		private String ability = getProperty("ability.name");
		private String abilityDescription = getProperty("ability.description");
		
		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			AtelierCharacter character = user.getSelectedCharacter();
			Ability ability = Ability.valueOf(event.getOption(this.ability).getAsString());
			int roll = (int) (Math.random() * 20 + 1);
			event.replyEmbeds(new RollSavingEmbedBuilder(ability, roll, character.getSavingModifier(ability)).build()).queue();
		}
		
		@Override
		public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
			if(event.getFocusedOption().getName().equals(ability)) {
				event.replyChoices(Stream.of(Ability.values())
					.filter(a -> a.toString().startsWith(event.getFocusedOption().getValue()))
					.map(a -> new Command.Choice(a.toString(), a.name()))
					.collect(Collectors.toList())).queue();
			}
		}
		
		@Override
		public void addOptions(SubcommandData data) {
			data.addOption(OptionType.STRING, ability, abilityDescription, true, true);
		}
	}
	
	private class RollSkill extends BaseSubcommand {

		private String skill = getProperty("skill.name");
		private String skillDescription = getProperty("skill.description");
		
		@Override
		public void execute(AtelierUser user, SlashCommandInteractionEvent event) {
			AtelierCharacter character = user.getSelectedCharacter();
			Skill skill = Skill.valueOf(event.getOption(this.skill).getAsString());
			int roll = (int) (Math.random() * 20 + 1);
			event.replyEmbeds(new RollSkillEmbedBuilder(skill, roll, character.getSkillModifier(skill)).build()).queue();
		}
		
		@Override
		public void complete(AtelierUser user, CommandAutoCompleteInteractionEvent event) {
			if(event.getFocusedOption().getName().equals(skill)) {
				event.replyChoices(Stream.of(Skill.values())
					.filter(s -> s.toString().startsWith(event.getFocusedOption().getValue()))
					.map(s -> new Command.Choice(s.toString(), s.name()))
					.collect(Collectors.toList())).queue();
			}
		}
		
		@Override
		public void addOptions(SubcommandData data) {
			data.addOption(OptionType.STRING, skill, skillDescription, true, true);
		}
	}
	
	public RollCommand() {
		registerSubcommand(new RollSaving());
		registerSubcommand(new RollSkill());
	}
}
