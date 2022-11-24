package com.atelier.console.commands;

import org.slf4j.Logger;

import com.atelier.AtelierBot;
import com.atelier.console.BaseConsoleCommand;

public class StopConsoleCommand extends BaseConsoleCommand {

	private String stopMessage = getMessage("stop").get();
	
	public StopConsoleCommand() {
		super("stop");
	}

	@Override
	public void execute(String[] args, Logger logger) {
		logger.info(stopMessage);
		AtelierBot.getInstance().stop();
	}

}
