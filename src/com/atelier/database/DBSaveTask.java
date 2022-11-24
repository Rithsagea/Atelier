package com.atelier.database;

import com.atelier.AtelierTask;

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
