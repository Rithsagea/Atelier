package com.atelier.dnd.types.alpha;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class TypeRegistry {
	
	private Map<String, Factory<?>> factoryMap;
	
	public TypeRegistry() {
		factoryMap = new HashMap<>();
	}
	
	public <T> void registryType(String name, Factory<T> factory) {
		factoryMap.put(name, factory);
	}
	
	public <T> void registerType(String name, Class<T> clazz) {
		factoryMap.put(name, new DefaultFactory<T>(clazz));
	}
	
	public Factory<?> getFactory(String name) {
		return factoryMap.get(name);
	}
	
	public void configureMapper(ObjectMapper mapper) {
		factoryMap.forEach((name, factory) -> {
			mapper.registerSubtypes(new NamedType(factory.getType(), name));
		});
	}
}
