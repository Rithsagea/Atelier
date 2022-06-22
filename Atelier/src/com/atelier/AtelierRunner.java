package com.atelier;

public class AtelierRunner {
	public static void main(String[] args) {
		AtelierBot bot = new AtelierBot("config.properties");

		bot.init();
	}
}
