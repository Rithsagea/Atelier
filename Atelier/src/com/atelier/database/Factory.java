package com.atelier.database;

import org.bson.codecs.Codec;

public interface Factory<T> extends Codec<T> {
	public T build();
}
