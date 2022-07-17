package com.atelier.database;

import com.atelier.discord.User;

public class UserFactory extends Factory<User> {

	private long id = -1;
	
	public UserFactory() {
		super(User.class);
	}
	
	public void setId(long id) { this.id = id; }

	@Override
	public User build() {
		return new User(id);
	}

}
