package com.atelier.discord;

import com.atelier.database.Factory;
import com.atelier.database.annotations.Constructor;
import com.atelier.database.annotations.Id;
import com.atelier.discord.AtelierUser.UserFactory;

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

	// MUTATORS

	public void setName(String name) {
		this.name = name;
	}
}
