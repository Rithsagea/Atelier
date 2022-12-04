package com.atelier.discord.embeds.dnd;

import com.atelier.discord.AtelierEmbedBuilder;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.util.WordUtil;

public class RollEmbedBuilder extends AtelierEmbedBuilder {
	
	public static class RollSavingEmbedBuilder extends RollEmbedBuilder {
		public RollSavingEmbedBuilder(Ability ability, int roll, int modifier) {
			setTitle(getMessage("title").addAbility(ability).get());
			addField(ability.toString(), getMessage("roll")
					.add("roll", roll)
					.add("modifier", WordUtil.formatModifier(modifier))
					.add("value", roll + modifier)
					.get(), true);
		}
	}
	
	public static class RollSkillEmbedBuilder extends RollEmbedBuilder {
		public RollSkillEmbedBuilder(Skill skill, int roll, int modifier) {
			setTitle(getMessage("title").addSkill(skill).get());
			addField(skill.toString(), getMessage("roll")
					.add("roll", roll)
					.add("modifier", WordUtil.formatModifier(modifier))
					.add("value", roll + modifier)
					.get(), true);
		}
	}
}
