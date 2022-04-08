package com.rithsagea.atelier;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rithsagea.atelier.discord.CommandRegistry;
import com.rithsagea.atelier.discord.LoginListener;
import com.rithsagea.atelier.discord.MessageListener;
import com.rithsagea.atelier.discord.StopCommand;
import com.rithsagea.atelier.discord.WaifuCommand;
import com.rithsagea.atelier.discord.menu.AtelierMenuManager;
import com.rithsagea.atelier.discord.music.AtelierAudioManager;
import com.rithsagea.atelier.discord.music.MusicCommand;
import com.rithsagea.atelier.dnd.database.AtelierDB;
import com.rithsagea.atelier.dnd.database.DBSaveTask;
import com.rithsagea.atelier.dnd.discord.CharacterCommand;

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
	private AtelierMenuManager menuManager;
	
	private boolean running;
	
	public AtelierBot(Config config) {
		System.setProperty("http.agent", "Chrome");
		
		this.config = config;
		
		db = new AtelierDB(config);
		commandRegistry = new CommandRegistry();
		logger = LoggerFactory.getLogger("Atelier");
		running = true;
		
		scheduler = Executors.newScheduledThreadPool(10);
		
		audioManager = new AtelierAudioManager();
		menuManager = new AtelierMenuManager();
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
		registerCommands();
		
		jda.addEventListener(new LoginListener(this));
		jda.addEventListener(new MessageListener(this));
	}
	
	private void registerTasks() {
		scheduler.scheduleAtFixedRate(new DBSaveTask(db), 15, 300, TimeUnit.SECONDS);
	}
	
	private void registerCommands() {
		commandRegistry.registerCommand(new StopCommand(this));
		
		commandRegistry.registerCommand(new WaifuCommand());
		
		commandRegistry.registerCommand(new MusicCommand(this));
		commandRegistry.registerCommand(new CharacterCommand(this));
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
	
	public AtelierMenuManager getMenuManager() {
		return menuManager;
	}
	
	public void stop() {
		running = false;
		jda.shutdownNow();
		audioManager.shutdown();
		db.disconnect();
	}
		
	public boolean isRunning() {
		return running;
	}
}
