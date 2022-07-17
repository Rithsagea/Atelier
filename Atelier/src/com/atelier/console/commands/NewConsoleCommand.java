package com.atelier.console.commands;

import org.slf4j.Logger;

import com.atelier.console.BaseConsoleSubcommand;
import com.atelier.console.GroupConsoleCommand;
import com.atelier.database.AtelierDB;
import com.atelier.dnd.types.Sheet;

public class NewConsoleCommand extends GroupConsoleCommand {
	
	private class NewSheet extends BaseConsoleSubcommand {

		public NewSheet() {
			super("sheet");
		}

		@Override
		public void execute(String[] args, Logger logger) {
			AtelierDB db = AtelierDB.getInstance();
			Sheet sheet = new Sheet();
			
			db.addSheet(sheet);
			
			logger.info("Created new sheet with id: {}", sheet.getId());
		}
		
	}
	
	public NewConsoleCommand() {
		super("new");
		registerSubcommand(new NewSheet());
	}

	@Override
	public void executeDefault(String[] args, Logger logger) {}
}
