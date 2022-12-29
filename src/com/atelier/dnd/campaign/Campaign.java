package com.atelier.dnd.campaign;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	private Map<UUID, Scene> scenes = new HashMap<>();

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Stream<AtelierCharacter> getCharacters() {
		return characters.stream().map(AtelierDB.getInstance()::getCharacter);
	}

	public Scene getScene(UUID id) {
		return scenes.get(id);
	}

	public Stream<Scene> listScenes() {
		return scenes.values().stream();
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

	public void addScene(Scene scene) {
		scenes.put(scene.getId(), scene);
	}

	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}
}
