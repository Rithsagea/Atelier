package com.atelier.database;

import java.util.HashMap;
import java.util.Map;

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
	
	private Map<Class<?>, Factory<?>> factories;
	private Map<Class<?>, Codec<?>> codecs;
	private CodecRegistry codecRegistry;
	
	private TypeRegistry() {
		codecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(this));
		
		factories = new HashMap<>();
		codecs = new HashMap<>();
		
		registerType(new UserFactory());
	}
	
	public <T> void registerType(Factory<T> factory) {
		Class<T> clazz = factory.getTypeClass();
		factories.put(clazz, factory);
		codecs.put(clazz, new BaseCodec<T>(clazz));
	}
	
	@SuppressWarnings("unchecked")
	public <T> Factory<T> getFactory(Class<T> type) {
		return (Factory<T>) factories.get(type);
	}
	
	public CodecRegistry getCodecRegistry() {
		return codecRegistry;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		return (Codec<T>) codecs.get(clazz);
	}

}
