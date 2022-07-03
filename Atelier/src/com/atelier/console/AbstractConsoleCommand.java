package com.atelier.console;

import org.slf4j.Logger;

public interface AbstractConsoleCommand {
	public String getLabel();
	public String[] getAliases();
	
	public void execute(String[] args, Logger logger);
}
