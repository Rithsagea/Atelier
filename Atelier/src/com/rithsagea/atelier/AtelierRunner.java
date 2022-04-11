package com.rithsagea.atelier;

public class AtelierRunner {
	public static void main(String[] args) {	
		AtelierBot bot = new AtelierBot("config.properties");
		
		bot.init();
		
		while(bot.isRunning());
	}
}
