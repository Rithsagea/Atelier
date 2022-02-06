package com.rithsagea.atelier;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rithsagea.atelier.listeners.MessageListener;
import com.rithsagea.atelier.music.MusicCommand;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class AtelierBot {
	
	private Config config;
	private CommandRegistry commandRegistry;
	private Logger logger;
	private JDA jda;
	
	private boolean running;
	
	public AtelierBot(Config config) {
		this.config = config;
		
		commandRegistry = new CommandRegistry();
		logger = LoggerFactory.getLogger("Atelier");
		running = true;
	}
	
	public void init() {
		logger.info("Initializing Atelier");
		
		JDABuilder builder = JDABuilder.createDefault(config.getDiscordToken());
		try {
			jda = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		registerCommands();
		
		jda.addEventListener(new MessageListener(this));
	}
	
	private void registerCommands() {
		commandRegistry.registerCommand(new PingCommand());
		commandRegistry.registerCommand(new MusicCommand());
	}
	
	public Config getConfig() {
		return config;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public CommandRegistry getCommandRegistry() {
		return commandRegistry;
	}
	
	public void stop() {
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
}
