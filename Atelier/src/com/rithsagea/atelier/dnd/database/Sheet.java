package com.rithsagea.atelier.dnd.database;

import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;

public class Sheet {
	
	private final UUID id;
	
	@BsonCreator
	public Sheet(@BsonId UUID id) {
		this.id = id;
	}
	
	public Sheet() {
		this(UUID.randomUUID());
	}
	
	//ACCESSORS
	
	public UUID getId() {
		return id;
	}
}
