package com.rithsagea.util.rand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Die {
	private int value;
	private int count;
	
	public Die(int count, int value) {
		this.value = value;
		this.count = count;
	}
	
	public Die(int value) {
		this(value, 1);
	}
	
	public int getValue() {
		return value;
	}
	
	public int getCount() {
		return count;
	}
	
	public List<Integer> roll(Random rand) {
		List<Integer> nums = new ArrayList<>();
		for(int x = 0; x < count; x++)
			nums.add(rand.nextInt(value) + 1);
		
		return Collections.unmodifiableList(nums);
	}
	
	public List<Integer> roll() {
		return roll(new Random());
	}
	
	public String toString() {
		return String.format("%dd%d", count, value);
	}
}
