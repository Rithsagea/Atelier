package com.atelier.dnd.campaign;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import com.atelier.database.AtelierDB;
import com.atelier.dnd.character.AtelierCharacter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Campaign {
	
	@JsonProperty("_id")
	private UUID id = UUID.randomUUID();
	private String name = "";

	private Set<UUID> characters = new HashSet<>();

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Stream<AtelierCharacter> getCharacters() {
		return characters.stream().map(AtelierDB.getInstance()::getCharacter);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addCharacter(AtelierCharacter character) {
		character.setCampaign(this);
		characters.add(character.getId());
	}

	public boolean removeCharacter(AtelierCharacter character) {
		character.setCampaign(null);
		return characters.remove(character.getId());
	}

	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}
}
