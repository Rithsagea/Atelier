package com.atelier.discord;

import com.atelier.database.annotations.Id;

public class User {

	@Id
	private final long id;
	private String name;
	
	//TODO remove this
	public User() {
		id = 0l;
	}
	
	public User(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("%s [%d]", name, id);
	}

	// ACCESSORS

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getTag() {
		return String.format("<@%d>", id);
	}

	// MUTATORS

	public void setName(String name) {
		this.name = name;
	}
}
