package com.atelier.dnd;

import java.util.EnumMap;
import java.util.stream.Stream;

import com.rithsagea.util.data.MathUtil;

public class AbilitySpread {
	private EnumMap<Ability, Integer> abilityScores;
	private int points;
	
	private static final int[] ABILITY_COST = {0, 1, 2, 3, 4, 5, 7, 9};
	private static final int MIN_SCORE = 8;
	private static final int MAX_SCORE = 15;
	
	public AbilitySpread() {
		abilityScores = new EnumMap<>(Ability.class);
		Stream.of(Ability.values()).forEach(a -> abilityScores.put(a, 0));
		points = 27;
	}
	
	public int getScore(Ability ability) {
		return abilityScores.get(ability);
	}
	
	public void setScore(Ability ability, int score) {
		if(!MathUtil.within(MIN_SCORE, MAX_SCORE, score))
			throw new RuntimeException("Invalid Score");
		
		int newPoints = points - ABILITY_COST[abilityScores.get(ability)] + ABILITY_COST[score];
		if(!MathUtil.within(0, 27, newPoints))
			throw new RuntimeException("Insufficient Points");
		
		points = newPoints;
		abilityScores.put(ability, score);
	}
}
