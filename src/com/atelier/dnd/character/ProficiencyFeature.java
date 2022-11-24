package com.atelier.dnd.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.atelier.database.AtelierType;
import com.atelier.discord.AtelierMenu;
import com.atelier.discord.Menu;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSavingProficiencyEvent;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSkillProficiencyEvent;
import com.rithsagea.util.event.EventHandler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

@AtelierType("proficiency")
public class ProficiencyFeature extends ClassFeature {

	private List<Ability> savingProficiencies = new ArrayList<>();
	private List<Skill> skillProficiencies = new ArrayList<>();

	private int skillCount;
	private Set<Integer> selectedSkills = Collections.emptySet();

	public ProficiencyFeature() { this(0); }

	/**
	 * @param skillCount    the amount of skills that can be selected
	 * @param proficiencies a list of all the proficiencies
	 */
	public ProficiencyFeature(int skillCount, Object... proficiencies) {
		this.skillCount = skillCount;

		for (Object proficiency : proficiencies) {
			if (proficiency instanceof Ability)
				savingProficiencies.add((Ability) proficiency);
			if (proficiency instanceof Skill)
				skillProficiencies.add((Skill) proficiency);
		}
	}

	@EventHandler
	public void onLoadSaving(LoadSavingProficiencyEvent event) {
		savingProficiencies.forEach(event::addProficiency);
	}

	@EventHandler
	public void onLoadSkill(LoadSkillProficiencyEvent event) {
		selectedSkills.forEach(s -> event.addProficiency(skillProficiencies.get(s)));
	}

	private class ProficiencyMenu implements AtelierMenu {
		@Override
		public Message initialize() {
			EmbedBuilder eb = new EmbedBuilder();
			MessageBuilder mb = new MessageBuilder();
			StringBuilder content = new StringBuilder();

			eb.setTitle(getMessage("title").get());

			savingProficiencies.forEach(a -> {
				content.append(a);
				content.append(", ");
			}); content.setLength(content.length() - 2);
			eb.addField(getMessage("saving.title").get(), content.toString(), false);

			content.setLength(0);
			content.append(getMessage("skill.prefix").add("count", skillCount));
			for(int x = 0; x < skillProficiencies.size(); x++) {
				content.append(String.format(selectedSkills.contains(x) ? "__%s__" : "%s", skillProficiencies.get(x)));
				content.append(", ");
			} content.setLength(content.length() - 2);
			eb.addField(getMessage("skill.title").get(), content.toString(), false);

			mb.setEmbeds(eb.build());
			mb.setActionRows(ActionRow.of(
				SelectMenu.create("skills")
					.setMaxValues(skillCount)
					.addOptions(IntStream.range(0, skillProficiencies.size()).boxed()
						.map(i -> SelectOption.of(skillProficiencies.get(i).getName(), Integer.toString(i)))
						.collect(Collectors.toList()))
					.setDefaultValues(selectedSkills.stream()
						.map(i -> Integer.toString(i))
						.collect(Collectors.toList()))
					.build()));
			return mb.build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) {}

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) {
			selectedSkills = event.getSelectedOptions().stream()
				.map(o -> Integer.parseInt(o.getValue()))
				.collect(Collectors.toSet());
			getCharacter().reload();
			event.editMessage(initialize()).queue();
		}
	}

	@Override
	public Menu getMenu() {
		return new ProficiencyMenu();
	}
}
