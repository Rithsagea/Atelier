package com.rithsagea.atelier.dnd.types.spread;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rithsagea.atelier.dnd.Ability;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	@Type(value = PointBuySpread.class, name = "pointBuy")
})
public interface AbilitySpread {
	/**
	 * Gets the base score of an ability
	 * @param ability the ability of the base score
	 * @return the base score
	 */
	public int getBaseScore(Ability ability);
}
