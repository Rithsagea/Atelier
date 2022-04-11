package com.rithsagea.atelier;

public class AtelierRunner {
	public static void main(String[] args) {	
		AtelierBot bot = new AtelierBot("config.properties");
		
		bot.init();
		
		//For when MongoDB breaks
//		AtelierDB db = bot.getDatabase();
//		User user = db.getUser(171378138041942016l);
//		Sheet sheet = new Sheet(UUID.fromString("1ced756c-dbc6-4be5-af0f-d6e66beffb0e"));
//		
//		user.setLevel(PermissionLevel.ADMINISTRATOR);
//		user.setSheetId(sheet.getId());
//		DBTest.initializeLita(sheet);
//		db.addSheet(sheet);
		
		while(bot.isRunning());
	}
}
