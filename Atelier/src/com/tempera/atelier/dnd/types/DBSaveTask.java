package com.tempera.atelier.dnd.types;

import com.tempera.atelier.AtelierTask;

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
