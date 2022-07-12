package com.atelier.dnd.types.alpha;

public interface Factory<T> {
	public Class<T> getType();
	public T build();
}
