package com.rithsagea.util;

public class WordUtil {
	public static String capitalize(String word) {
		return Character.toUpperCase(word.charAt(0)) + 
				word.toLowerCase().substring(0, word.length());
	}
}
