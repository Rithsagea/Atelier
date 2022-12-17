package com.atelier.dnd.campaign;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import com.atelier.database.AtelierDB;
import com.atelier.dnd.character.AtelierCharacter;

public class Scene {
  private UUID id = UUID.randomUUID();
  private String name = "";

  private Set<UUID> characters = new HashSet<>();

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void addCharacter(AtelierCharacter character) {
    characters.add(character.getId());
  }

  public void removeCharacter(AtelierCharacter character) {
    characters.remove(character.getId());
  }

  public void setName(String name) {
    this.name = name;
  }

  public Stream<AtelierCharacter> listCharacters() {
    return characters.stream().map(AtelierDB.getInstance()::getCharacter);
  }

  @Override
  public String toString() {
    return String.format("%s [%s]", name, id);
  }
}
