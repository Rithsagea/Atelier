package com.atelier.console;

import org.slf4j.Logger;

import com.atelier.AtelierObject;

public interface AbstractConsoleCommand extends AtelierObject {
	public String getLabel();
	public String[] getAliases();
	
	public void execute(String[] args, Logger logger);
}
