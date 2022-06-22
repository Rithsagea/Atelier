package com.atelier.discord.commands.character;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.atelier.AtelierLanguageManager;
import com.atelier.discord.User;
import com.atelier.discord.commands.BaseInteraction.BaseSubcommandGroup;
import com.atelier.dnd.types.enums.Ability;
import com.atelier.dnd.types.enums.Skill;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CharacterRollCommand extends BaseSubcommandGroup {

	private class RollSaving extends BaseSubcommand {
		
		private final String optionAbilityName = AtelierLanguageManager.getInstance().get(this, "ability.name");
		private final String optionAbilityDescription = AtelierLanguageManager.getInstance().get(this, "ability.description");
		
		@Override
		public void execute(User user, SlashCommandInteractionEvent event) {
			Ability a = Ability.fromString(event.getOption("ability").getAsString());
			int roll = (int) (Math.random() * 20 + 1);
			int modifier = user.getSheet().getAbilityModifier(a);
			event.replyFormat("%s Check: %d (%d + %d)", a, roll + modifier, roll, modifier).queue();
		}

		@Override
		public void complete(User user, CommandAutoCompleteInteractionEvent event) {
			event.replyChoiceStrings((Stream.of(Ability.values()).map(a -> a.toString())).collect(Collectors.toList())).queue();
		}

		@Override
		public void addOptions(SubcommandData data) {
			data.addOption(OptionType.STRING, optionAbilityName, optionAbilityDescription, true, true);
		}	
	}
	
	private class RollSkill extends BaseSubcommand {
		
		private final String optionSkillName = AtelierLanguageManager.getInstance().get(this, "skill.name");
		private final String optionSkillDescription = AtelierLanguageManager.getInstance().get(this, "skill.description");
		
		@Override
		public void execute(User user, SlashCommandInteractionEvent event) {
			Skill s = Skill.fromString(event.getOption("skill").getAsString());
			int roll = (int) (Math.random() * 20 + 1);
			int modifier = user.getSheet().getSkillModifier(s);
			event.replyFormat("%s Check: %d (%d + %d)", s, roll + modifier, roll, modifier).queue();
		}

		@Override
		public void complete(User user, CommandAutoCompleteInteractionEvent event) {
			event.replyChoiceStrings((Stream.of(Skill.values()).map(a -> a.toString())).collect(Collectors.toList())).queue();
		}

		@Override
		public void addOptions(SubcommandData data) {
			data.addOption(OptionType.STRING, optionSkillName, optionSkillDescription, true, true);
		}	
	}
	
	public CharacterRollCommand() {
		registerSubcommand(new RollSaving());
		registerSubcommand(new RollSkill());
	}

}
