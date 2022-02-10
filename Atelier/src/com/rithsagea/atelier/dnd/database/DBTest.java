package com.rithsagea.atelier.dnd.database;

import com.rithsagea.atelier.Config;

public class DBTest {
	public static void main(String[] args) {
		Config config = new Config("config.properties");
		AtelierDB db = new AtelierDB(config);
		
		User user = new User(69420l);
		user.setName("Lita");
		
		System.out.println(user.getName() + ": " + user.getId());
		
		db.updateUser(user);
		
		user = db.findUser(user.getId());
		
		System.out.println(user.getName() + ": " + user.getId());
		
	}
}
