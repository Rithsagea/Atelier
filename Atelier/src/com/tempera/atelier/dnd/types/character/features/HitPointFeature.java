package com.tempera.atelier.dnd.types.character.features;

import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.rand.Dice;
import com.rithsagea.util.rand.Die;
import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.IndexedItem;
import com.tempera.atelier.dnd.events.LoadHitPointsEvent;
import com.tempera.atelier.dnd.events.character.LevelUpClassEvent;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.enums.Ability;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

@IndexedItem("feature-hit-points")
public class HitPointFeature implements Attribute {
	
	private class HitPointMenu extends Menu {

		@Override
		public Message initialize() {
			return new MessageBuilder("hi").build();
		}

		@Override
		public void onButtonInteract(ButtonInteractionEvent event) { }

		@Override
		public void onSelectInteract(SelectMenuInteractionEvent event) { }
		
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
		
		e.addMaxHitPoints(hitDie.getValue() + e.getSheet().getAbilityModifier(Ability.CONSTITUTION));
		e.addMaxHitPoints((hitDie.getValue() / 2 + 1) * (hitDie.getCount() - 1));
	}
	
	@EventHandler
	public void onLevelUp(LevelUpClassEvent e) {
		hitDie = new Die(hitDie.getCount() + 1, hitDie.getValue());
	}

	@Override
	public Menu getMenu() {
		return new HitPointMenu();
	}
}
