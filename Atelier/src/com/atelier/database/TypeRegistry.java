package com.atelier.database;

import java.util.HashMap;
import java.util.Map;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClientSettings;

public class TypeRegistry implements CodecProvider {

	private static final TypeRegistry INSTANCE = new TypeRegistry();
	public static TypeRegistry getInstance() {
		return INSTANCE;
	}
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private CodecRegistry codecRegistry;
	private Map<Class<?>, AtelierCodec<?>> codecs = new HashMap<>();
	
	private TypeRegistry() {
		codecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(this));
		
		Types.registerTypes(this);
		System.out.println();
	}
	
	public <T> void registerType(Class<T> typeClass) {
		codecs.put(typeClass, new AtelierCodec<T>(typeClass));
		
		logger.info("Registered Type: " + typeClass);
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
