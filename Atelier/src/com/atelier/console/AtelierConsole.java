package com.atelier.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AtelierConsole implements Runnable {
	private BufferedReader reader;
	private ConsoleCommandRegistry registry;
	
	private Thread thread;
	private boolean running;
	
	public AtelierConsole(InputStream input) {
		reader = new BufferedReader(new InputStreamReader(input));
		registry = new ConsoleCommandRegistry();
		
		// register commands here
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
			if(cmd != null) cmd.execute(args);
			else System.out.println("Echo: " + line);
		}
	}

}
