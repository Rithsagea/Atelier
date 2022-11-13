package com.atelier.dnd.character;

import java.util.ArrayList;
import java.util.List;

import com.atelier.database.AtelierType;
import com.atelier.discord.AtelierMenu;
import com.atelier.discord.Menu;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.events.LoadProficiencyEvent.LoadSavingProficiencyEvent;
import com.rithsagea.util.event.EventHandler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

@AtelierType("proficiency")
public class ProficiencyFeature extends ClassFeature {

	private List<Ability> savingProficiencies = new ArrayList<>();
	private List<Skill> skillProficiencies = new ArrayList<>();

	private int skillCount;
	private List<Integer> selectedSkills;

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
			skillProficiencies.forEach(s -> {
				content.append(s);
				content.append(", ");
			}); content.setLength(content.length() - 2);
			eb.addField(getMessage("skill.title").get(), content.toString(), false);

			mb.setEmbeds(eb.build());
			return mb.build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) {

		}

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) {

		}
	}

	@Override
	public Menu getMenu() {
		return new ProficiencyMenu();
	}
}
