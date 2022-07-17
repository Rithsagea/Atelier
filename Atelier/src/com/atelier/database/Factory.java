package com.atelier.database;

public abstract class Factory<T> {
	
	private Class<T> typeClass;
	
	public Factory(Class<T> typeClass) {
		this.typeClass = typeClass;
	}
	
	public Class<T> getTypeClass() {
		return typeClass;
	}
	
	public abstract T build();
}
