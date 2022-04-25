package com.tempera.util;

public class Dice {
	private int value;
	private int count;
	
	public Dice(int value, int count) {
		this.value = value;
		this.count = count;
	}
	
	public Dice(int value) {
		this(value, 1);
	}
	
	public int getValue() {
		return value;
	}
	
	public int getCount() {
		return count;
	}
	
	public String toString() {
		return String.format("%dd%d", count, value);
	}
}
