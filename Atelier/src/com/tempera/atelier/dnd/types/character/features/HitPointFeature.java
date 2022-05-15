package com.tempera.atelier.dnd.types.character.features;

import com.rithsagea.util.event.EventHandler;
import com.rithsagea.util.rand.Dice;
import com.rithsagea.util.rand.Die;
import com.tempera.atelier.dnd.IndexedItem;
import com.tempera.atelier.dnd.events.LoadHitPointsEvent;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.enums.Ability;

@IndexedItem("feature-hit-points")
public class HitPointFeature implements Attribute {
	
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
}
