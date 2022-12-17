package com.atelier.discord.embeds.dnd;

import com.atelier.discord.AtelierEmbedBuilder;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.character.AtelierCharacter;
import com.atelier.util.WordUtil;

public class CharacterInfoEmbedBuilder extends AtelierEmbedBuilder {
	public CharacterInfoEmbedBuilder(AtelierCharacter character) {
		StringBuilder content = new StringBuilder();
		String prefix;
		
		setTitle(getMessage("title").addCharacter(character).get());
		
		content.append(getMessage("description.campaign")
			.addCampaign(character.getCampaign()).get() + "\n");
		content.append(getMessage("description.scene")
			.addScene(character.getScene()).get());
		this.setDescription(content.toString());

		prefix = "";
		content.setLength(0);
		for(Ability ability : Ability.values()) {
			content.append(prefix);

			if(character.hasSavingProficiency(ability)) content.append("__");
			content.append(getMessage("ability")
				.addAbility(ability)
				.add("abilityScore", character.getAbilityScore(ability))
				.add("abilityModifier", WordUtil.formatModifier(character.getAbilityModifier(ability)))
				.get());
			if(character.hasSavingProficiency(ability)) content.append("__");

			prefix = "\n";
		}
		addField(getMessage("ability.title").get(), content.toString(), true);

		prefix = "";
		content.setLength(0);
		for(Ability ability : Ability.values()) {
			content.append(prefix);
			content.append(getMessage("saving")
				.addAbility(ability)
				.add("savingModifier", WordUtil.formatModifier(character.getSavingModifier(ability)))
				.get());
			prefix = "\n";
		}
		addField(getMessage("saving.title").get(), content.toString(), true);

		prefix = "";
		content.setLength(0);
		for(Skill skill : Skill.values()) {
			content.append(prefix);
			if(character.hasSkillProficiency(skill)) content.append("__");
			content.append(getMessage("skill")
				.addSkill(skill)
				.add("skillModifier", WordUtil.formatModifier(character.getSkillModifier(skill)))
				.get());
			if(character.hasSkillProficiency(skill)) content.append("__");
			prefix = "\n";
		}
		addField(getMessage("skill.title").get(), content.toString(), false);
	}
}
