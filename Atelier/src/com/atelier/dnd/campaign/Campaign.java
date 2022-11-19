package com.atelier.dnd.campaign;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Campaign {
	
	@JsonProperty("_id")
	private UUID id = UUID.randomUUID();
	private String name = "";

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}
}
