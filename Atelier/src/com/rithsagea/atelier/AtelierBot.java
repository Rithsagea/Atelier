package com.rithsagea.atelier;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rithsagea.atelier.discord.LoginListener;
import com.rithsagea.atelier.discord.MessageListener;
import com.rithsagea.atelier.discord.commands.CommandRegistry;
import com.rithsagea.atelier.discord.music.AtelierAudioManager;
import com.rithsagea.atelier.dnd.database.AtelierDB;
import com.rithsagea.atelier.dnd.database.DBSaveTask;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class AtelierBot {
	
	private AtelierDB db;
	private Config config;
	private CommandRegistry commandRegistry;
	private Logger logger;
	private JDA jda;
	
	private ScheduledExecutorService scheduler;
	
	private AtelierAudioManager audioManager;
	
	private boolean running;
	
	public AtelierBot(String configPath) {
		System.setProperty("http.agent", "Chrome");
		
		this.config = new Config(configPath);
		
		db = new AtelierDB(config);
		commandRegistry = new CommandRegistry();
		logger = LoggerFactory.getLogger("Atelier");
		running = true;
		
		scheduler = Executors.newScheduledThreadPool(10);
		
		audioManager = new AtelierAudioManager();
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
		
		registerTasks();
		
		jda.addEventListener(new LoginListener(this));
		jda.addEventListener(new MessageListener(this));
	}
	
	private void registerTasks() {
		scheduler.scheduleAtFixedRate(new DBSaveTask(db), 15, 300, TimeUnit.SECONDS);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public AtelierDB getDatabase() {
		return db;
	}
	
	public CommandRegistry getCommandRegistry() {
		return commandRegistry;
	}
	
	public AtelierAudioManager getAudioManager() {
		return audioManager;
	}
	
	public void stop() {
		jda.shutdown();
		audioManager.shutdown();
		db.disconnect();
		running = false;
	}
		
	public boolean isRunning() {
		return running;
	}
}
