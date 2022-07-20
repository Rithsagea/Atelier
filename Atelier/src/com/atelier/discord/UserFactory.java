package com.atelier.discord;

import com.atelier.database.Factory;

public class UserFactory implements Factory<User> {

	private long id = -1;
	
	public void setId(long id) { this.id = id; }

	@Override
	public User build() {
		return new User(id);
	}

}
