package com.rithsagea.atelier;

public class AtelierRunner {
	public static void main(String[] args) {
		Config config = new Config("config.properties");		
		AtelierBot bot = new AtelierBot(config);
		
		bot.init();
		
		while(bot.isRunning());
	}
}
