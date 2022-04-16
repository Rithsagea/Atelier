package com.rithsagea.util;

public class WordUtil {
	public static String capitalize(String word) {
		return Character.toUpperCase(word.charAt(0)) + 
				word.toLowerCase().substring(1, word.length());
	}
	
	public static String formatModifier(int modifier) {
		if(modifier <= 0) return Integer.toString(modifier);
		return "+" + modifier;
	}
}
