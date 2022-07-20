package com.atelier.database;

public interface Factory<T> {
	public abstract T build();
}
