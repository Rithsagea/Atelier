package com.atelier.dnd.types.spread;

import java.util.HashMap;
import java.util.Map;

import com.atelier.dnd.types.enums.Ability;

public class PointBuySpread implements AbilitySpread {

	private Map<Ability, Integer> scores;
	private int points;

	public PointBuySpread() {
		scores = new HashMap<>();
		points = 27;
	}

	/**
	 * Returns how many points are remaining
	 * 
	 * @return the amount of points remaining
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Sets the score of an ability. Changes points accordingly. Does not allow
	 * erroneous values
	 * 
	 * @param ability the ability to change
	 * @param value   the value of the ability score
	 * @return whether the score was successfully set
	 */
	public boolean setScore(Ability ability, int value) {
		int cost = getCost(value);
		if (cost == -1)
			return false; // invalid value

		int score = getBaseScore(ability);

		if (cost <= points + getCost(score)) { // successfully set
			points = points + getCost(score) - cost;
			scores.put(ability, value);
			return true;
		}

		return false;
	}

	@Override
	public int getBaseScore(Ability ability) {
		if (!scores.containsKey(ability))
			scores.put(ability, 8); // default value
		return scores.get(ability);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PointBuy: [");
		String prefix = "";
		for (Ability ability : Ability.values()) {
			builder.append(prefix);
			builder.append(getBaseScore(ability));
			prefix = ", ";
		}
		builder.append("] [");
		builder.append(points);
		builder.append("]");

		return builder.toString();
	}

	/**
	 * Returns the cost of a given score. For the 5e Point Buy system
	 * 
	 * @param score the base score
	 * @return the point cost of the score
	 */
	private static int getCost(int score) {
		switch (score) {
		case 8:
			return 0;
		case 9:
			return 1;
		case 10:
			return 2;
		case 11:
			return 3;
		case 12:
			return 4;
		case 13:
			return 5;
		case 14:
			return 7;
		case 15:
			return 9;

		default:
			return -1;
		}
	}

}
