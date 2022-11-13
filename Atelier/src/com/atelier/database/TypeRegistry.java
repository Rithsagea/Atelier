package com.atelier.database;

import java.util.HashMap;
import java.util.Map;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.mongodb.MongoClientSettings;

public class TypeRegistry implements CodecProvider {

	private static final TypeRegistry INSTANCE = new TypeRegistry();
	public static TypeRegistry getInstance() {
		return INSTANCE;
	}
	
	private CodecRegistry codecRegistry;
	private Map<Class<?>, AtelierCodec<?>> codecs = new HashMap<>();
	
	private ObjectMapper mapper;
	
	private TypeRegistry() {
		codecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(this));
		
		mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);

		Reflections reflections = new Reflections(
			new ConfigurationBuilder()
				.forPackage("")
				.setParallel(true)
		);

		mapper.registerSubtypes(
			reflections.getTypesAnnotatedWith(AtelierType.class)
			.stream()
			.map(t -> new NamedType(t, t.getAnnotation(AtelierType.class).value()))
			.toArray(NamedType[]::new));
	}
	
	public CodecRegistry getCodecRegistry() {
		return codecRegistry;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		if(!codecs.containsKey(clazz)) codecs.put(clazz, new AtelierCodec<T>(clazz, mapper));
		return (Codec<T>) codecs.get(clazz);
	}
}
