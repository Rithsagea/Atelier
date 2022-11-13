package com.atelier.database;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private Logger log = LoggerFactory.getLogger("Types");
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

		Set<Class<?>> subtypes = reflections.getTypesAnnotatedWith(AtelierType.class);
		mapper.registerSubtypes(subtypes.stream()
			.map(t -> new NamedType(t, t.getAnnotation(AtelierType.class).value()))
			.toArray(NamedType[]::new));

		subtypes.stream()
			.sorted(new Comparator<Class<?>>() {
				@Override
				public int compare(Class<?> a, Class<?> b) {
					return a.getName().compareTo(b.getName());
				}
			})
			.forEach(t -> log.debug(String.format("%s: %s", t.getAnnotation(AtelierType.class).value(), t.getName())));
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
