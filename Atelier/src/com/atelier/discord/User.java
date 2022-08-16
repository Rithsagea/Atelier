package com.atelier.discord;

import com.atelier.database.Factory;
import com.atelier.database.annotations.Constructor;
import com.atelier.database.annotations.Id;
import com.atelier.discord.User.UserFactory;

@Constructor(UserFactory.class)
public class User {
	
	public static class UserFactory implements Factory<User> {
		@Override
		public User build() {
			return new User(0);
		}
	}

	@Id
	private final long id;
	private String name;
	
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
