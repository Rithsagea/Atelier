package com.rithsagea.util.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Choice<E> {

	private List<E> choices;
	private TreeSet<Integer> selectedChoices;
	private int count;

	public Choice() {
		this(0);
	}

	@SafeVarargs
	public Choice(int count, E... choices) {
		this.count = count;
		this.choices = new ArrayList<>(Arrays.asList(choices));
		this.selectedChoices = new TreeSet<>();
	}

	public List<E> getChoices() {
		return Collections.unmodifiableList(choices);
	}

	public E getChoice(int index) {
		return choices.get(index);
	}

	public int getCount() {
		return count;
	}

	public Set<Integer> getSelected() {
		return Collections.unmodifiableSet(selectedChoices);
	}

	public boolean isSelected(int index) {
		return selectedChoices.contains(index);
	}

	public boolean selectChoice(int index) {
		if (selectedChoices.size() < count && selectedChoices.add(index))
			return true;
		return false;
	}

	public void clear() {
		selectedChoices.clear();
	}
}
