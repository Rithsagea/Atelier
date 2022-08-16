package com.atelier.console.commands;

import org.slf4j.Logger;

import com.atelier.AtelierBot;
import com.atelier.console.BaseConsoleCommand;

public class StopConsoleCommand extends BaseConsoleCommand {

	public StopConsoleCommand() {
		super("stop");
	}

	@Override
	public void execute(String[] args, Logger logger) {
		logger.info("Stopping Atelier Bot!");
		AtelierBot.getInstance().stop();
	}

}
