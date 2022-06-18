package com.tempera.atelier.dnd.types.character.features;

import java.awt.Color;

import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.rand.Dice;
import com.rithsagea.util.rand.Die;
import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.events.LoadClassEvent;
import com.tempera.atelier.dnd.events.LoadHitPointsEvent;
import com.tempera.atelier.dnd.events.LoadSheetEvent;
import com.tempera.atelier.dnd.events.character.LevelUpClassEvent;
import com.tempera.atelier.dnd.types.IndexedItem;
import com.tempera.atelier.dnd.types.character.AbstractClass;
import com.tempera.atelier.dnd.types.character.CharacterAttribute;
import com.tempera.atelier.dnd.types.enums.Ability;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

@IndexedItem("feature-hit-points")
public class HitPointFeature implements CharacterAttribute {

	private class HitPointMenu implements Menu {

		@Override
		public Message initialize() {
			MessageBuilder msg = new MessageBuilder();
			EmbedBuilder embed = new EmbedBuilder();
			StringBuilder content = new StringBuilder();

			content.append(String.format("**Hit Dice**: %s\n", hitDie));
			content.append(
				String.format("**Hit Points at 1st Level**: %d + %s modifier\n",
					hitDie.getValue(), Ability.CONSTITUTION));
			content.append(String.format(
				"**Hit Points at Higher Levels**: %s (or %s) + your %s modifier per %s level after 1st\n",
				hitDie, hitDie.getValue() / 2 + 1, Ability.CONSTITUTION,
				"Class"));

			embed.setTitle("**" + getName() + "**");
			embed.setDescription(content);
			embed.setColor(Color.GREEN);

			msg.setEmbeds(embed.build());

			return msg.build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) {
		}

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) {
		}

	}

	private Die hitDie;

	public HitPointFeature() {
		this(8);
	}

	public HitPointFeature(int diceValue) {
		this.hitDie = new Die(1, diceValue);
	}

	@Override
	public String getName() {
		return "Hit Points";
	}

	@EventHandler
	public void onLoadHitPoints(LoadHitPointsEvent e) {
		e.addHitDice(new Dice(hitDie));

		e.addMaxHitPoints(hitDie.getValue() + e.getSheet()
			.getAbilityModifier(Ability.CONSTITUTION));
		e.addMaxHitPoints(
			(hitDie.getValue() / 2 + 1) * (hitDie.getCount() - 1));
	}

	@EventHandler
	public void onLevelUp(LevelUpClassEvent e) {
		hitDie = new Die(hitDie.getCount() + 1, hitDie.getValue());
	}

	// TODO this will be useful
	@EventHandler
	private void onLoadClass(LoadClassEvent event) {
		AbstractClass c = event.getCharacterClass();
	}
	
	@EventHandler
	private void onLoadSheet(LoadSheetEvent event) {
		System.out.println(event.getSheet());
	}

	@Override
	public Menu getMenu() {
		return new HitPointMenu();
	}
}
