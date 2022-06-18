package com.tempera.atelier.discord.commands.character;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.BaseSubcommand;
import com.tempera.atelier.discord.commands.BaseSubcommandGroup;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CharacterRollCommand extends BaseSubcommandGroup {

	private class RollSaving extends BaseSubcommand {
		public RollSaving() {
			super("saving", "Roll for an saving check");
		}

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
			data.addOption(OptionType.STRING, "ability", "the ability to roll for", true, true);
		}	
	}
	
	private class RollSkill extends BaseSubcommand {
		public RollSkill() {
			super("skill", "Roll for a skill check");
		}

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
			data.addOption(OptionType.STRING, "skill", "the skill to roll for", true, true);
		}	
	}
	
	public CharacterRollCommand() {
		super("roll", "Roll for a character");
		registerSubcommand(new RollSaving());
		registerSubcommand(new RollSkill());
	}

}
