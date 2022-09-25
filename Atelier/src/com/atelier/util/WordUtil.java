package com.atelier.util;

public class WordUtil {
	
	public static String capitalize(String str) {
		str = str.toLowerCase();
		StringBuilder builder = new StringBuilder();
		String prefix = "";
		for (String word : str.split(" ")) {
			builder.append(prefix);
			builder.append(word.substring(0, 1)
				.toUpperCase() + word.substring(1, word.length()));
			prefix = " ";
		}
		return builder.toString();
	}

	public static String formatModifier(int modifier) {
		if (modifier <= 0)
			return Integer.toString(modifier);
		return "+" + modifier;
	}
}
