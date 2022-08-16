package com.atelier.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atelier.console.commands.StopConsoleCommand;
import com.atelier.console.commands.UserConsoleCommand;

public class AtelierConsole implements Runnable {
	private BufferedReader reader;
	private ConsoleCommandRegistry registry;
	private Logger logger;
	
	private Thread thread;
	private boolean running;
	
	public AtelierConsole(InputStream input) {
		reader = new BufferedReader(new InputStreamReader(input));
		registry = new ConsoleCommandRegistry();
		logger = LoggerFactory.getLogger("Console");
		
		// register commands here
		registry.registerCommand(new StopConsoleCommand());
		registry.registerCommand(new UserConsoleCommand());
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		running = true;
		String line = "";
		
		while(running) {
			try {
				line = reader.readLine();
			} catch (IOException e) { e.printStackTrace(); }
			
			String[] args = line.split(" ");
			AbstractConsoleCommand cmd = registry.getCommand(args[0]);
			if(cmd != null) {
				try {
					cmd.execute(args, logger); 
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Echo: " + line);
			}
		}
	}

}
