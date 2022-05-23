package com.rithsagea.util.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Choice<E> {
	
	private List<E> choices;
	private int count;
	
	public Choice() {
		this(0);
	}
	
	@SafeVarargs
	public Choice(int count, E... choices) {
		this.count = count;
		this.choices = new ArrayList<>(Arrays.asList(choices));
	}
	
	public List<E> getChoices() {
		return Collections.unmodifiableList(choices);
	}
	
	public int getCount() {
		return count;
	}
}
