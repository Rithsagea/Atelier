package com.atelier.discord;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.atelier.database.AtelierDB;
import com.atelier.database.Factory;
import com.atelier.database.annotations.Constructor;
import com.atelier.database.annotations.Id;
import com.atelier.discord.AtelierUser.UserFactory;
import com.atelier.dnd.AtelierCharacter;

@Constructor(UserFactory.class)
public class AtelierUser {
	
	public static class UserFactory implements Factory<AtelierUser> {
		@Override
		public AtelierUser build() {
			return new AtelierUser(0);
		}
	}

	@Id
	private final long id;
	private String name;
	private HashSet<UUID> characters = new HashSet<>();
	
	public AtelierUser(long id) {
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
}
