package com.rithsagea.util.rand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class Dice implements Cloneable {
	private TreeMap<Integer, Integer> dice;

	public Dice() {
		dice = new TreeMap<>(Comparator.reverseOrder());
	}

	public Dice(int count, int value) {
		this();

		addDie(count, value);
	}

	public Dice(Die die) {
		this(die.getCount(), die.getValue());
	}

	public void addDie(int count, int value) {
		if (!dice.containsKey(value))
			dice.put(value, 0);
		dice.put(value, dice.get(value) + count);
	}

	public void addDie(int value) {
		addDie(1, value);
	}

	public void addDie(Die die) {
		addDie(die.getCount(), die.getValue());
	}

	public void addDice(Dice dice) {
		for (int value : dice.getValues()) {
			addDie(dice.getCount(value), value);
		}
	}

	public Set<Integer> getValues() {
		return dice.keySet();
	}

	public int getCount(int value) {
		return dice.get(value);
	}

	public List<Integer> roll(Random rand) {
		List<Integer> nums = new ArrayList<>();
		for (Entry<Integer, Integer> die : dice.entrySet())
			for (int x = 0; x < die.getValue(); x++)
				nums.add(rand.nextInt(die.getKey()) + 1);

		return Collections.unmodifiableList(nums);
	}

	public List<Integer> roll() {
		return roll(new Random());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String prefix = "";

		for (Entry<Integer, Integer> die : dice.entrySet()) {
			builder.append(prefix);
			builder
				.append(String.format("%dd%d", die.getValue(), die.getKey()));
			prefix = " + ";
		}

		return builder.toString();
	}

	@Override
	public Dice clone() {
		Dice dice = new Dice();
		for (Entry<Integer, Integer> die : this.dice.entrySet())
			dice.addDie(die.getValue(), die.getKey());

		return dice;
	}
}
