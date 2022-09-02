package com.atelier.discord;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.atelier.database.AtelierDB;
import com.atelier.dnd.AtelierCharacter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AtelierUser {

	@JsonProperty("_id")
	private final long id;
	private String name;
	private HashSet<UUID> characters = new HashSet<>();
	private UUID selectedCharacter;
	
	@JsonCreator
	public AtelierUser(@JsonProperty("_id")long id) {
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
	
	public Set<AtelierCharacter> getCharacters() {
		AtelierDB db = AtelierDB.getInstance();
		return characters.stream()
				.map((UUID id) -> db.getCharacter(id))
				.collect(Collectors.toSet());
	}
	
	public AtelierCharacter getSelectedCharacter() {
		return AtelierDB.getInstance().getCharacter(selectedCharacter);
	}

	// MUTATORS

	public void setName(String name) {
		this.name = name;
	}
	
	public void addCharacter(AtelierCharacter character) {
		characters.add(character.getId());
	}
	
	public boolean removeCharacter(AtelierCharacter character) {
		return characters.remove(character.getId());
	}
	
	public void selectedCharacter(AtelierCharacter character) {
		if(characters.contains(character.getId()))
			selectedCharacter = character.getId();
		else
			throw new RuntimeException("User does not contain specified character");
	}
}
