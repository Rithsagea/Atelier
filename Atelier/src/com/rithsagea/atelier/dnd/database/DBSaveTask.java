package com.rithsagea.atelier.dnd.database;

import com.rithsagea.atelier.AtelierTask;

public class DBSaveTask implements AtelierTask {

	private AtelierDB db;
	
	public DBSaveTask(AtelierDB db) {
		this.db = db;
	}
	
	@Override
	public void run() {
		db.save();
	}

}
