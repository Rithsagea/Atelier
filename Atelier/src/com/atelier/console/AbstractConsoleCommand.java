package com.atelier.console;

public interface AbstractConsoleCommand {
	public String getLabel();
	public String[] getAliases();
	
	public void execute(String[] args);
}
