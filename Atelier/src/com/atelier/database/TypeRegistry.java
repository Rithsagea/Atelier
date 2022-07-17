package com.atelier.database;

import java.util.HashMap;
import java.util.Map;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.atelier.discord.User;
import com.mongodb.MongoClientSettings;

public class TypeRegistry implements CodecProvider {

	private static final TypeRegistry INSTANCE = new TypeRegistry();
	public static TypeRegistry getInstance() {
		return INSTANCE;
	}
	
	private Map<Class<?>, Codec<?>> factories;
	private CodecRegistry codecRegistry;
	
	private TypeRegistry() {
		codecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(this));
		
		factories = new HashMap<>();
		
		registerType(new BaseFactory<User>(User.class) {
			@Override
			public User build() {
				return new User(0l);
			}
		});
	}
	
	public void registerType(Factory<?> typeFactory) {
		factories.put(typeFactory.getEncoderClass(), typeFactory);
	}
	
	public CodecRegistry getCodecRegistry() {
		return codecRegistry;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		return (Codec<T>) factories.get(clazz);
	}

}
