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
	
	private Map<Class<?>, Factory<?>> factories;
	private Map<Class<?>, Codec<?>> codecs;
	private Map<Class<?>, Map<String, Class<?>>> subtypes;
	private CodecRegistry codecRegistry;
	
	private TypeRegistry() {
		codecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(this));
		
		factories = new HashMap<>();
		codecs = new HashMap<>();
		subtypes = new HashMap<>();
	}
	
	public <T> void registerType(Factory<T> factory) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) factory.build().getClass();
		registerType(clazz, factory, new AtelierCodec<T>(clazz));
	}
	
	public <T> void registerType(Factory<T> factory, Codec<T> codec) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) factory.build().getClass();
		registerType(clazz, factory, codec);
	}
	
	public <T> void registerType(Class<T> clazz, Factory<T> factory, Codec<T> codec) {
		factories.put(clazz, factory);
		codecs.put(clazz, codec);
	}
	
	//TODO change subtype to subtypefactory
	public void registerSubtype(Class<?> supertype, Class<?> subtype) {
		if(!subtypes.containsKey(supertype)) subtypes.put(supertype, new HashMap<>());
		Subtype ann = subtype.getAnnotation(Subtype.class);
		if(ann == null) logger.error("Missing Subtype Annotation: " + subtype);
		
		Map<String, Class<?>> subtypeMap = subtypes.get(supertype);
		subtypeMap.put(ann.value(), subtype);
		for(String alias : ann.aliases())
			subtypeMap.put(alias, subtype);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Factory<T> getFactory(Class<T> type) {
		return (Factory<T>) factories.get(type);
	}
	
	public CodecRegistry getCodecRegistry() {
		return codecRegistry;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> getSubtype(Class<T> supertype, String name) {
		return (Class<? extends T>) subtypes.get(supertype).get(name);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		return (Codec<T>) codecs.get(clazz);
	}
}
