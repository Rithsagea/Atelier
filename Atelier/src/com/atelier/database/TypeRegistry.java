package com.atelier.database;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.MongoClientSettings;

public class TypeRegistry implements CodecProvider {

	private static final TypeRegistry INSTANCE = new TypeRegistry();
	public static TypeRegistry getInstance() {
		return INSTANCE;
	}
	
	private CodecRegistry codecRegistry;
	
	private TypeRegistry() {
		codecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(this));
	}
	
	public CodecRegistry getCodecRegistry() {
		return codecRegistry;
	}

	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		return null;
	}
}
