package com.atelier.database;

public class AtelierType<T> {
	private final Class<T> typeClass;
	private Class<? extends Factory<T>> factoryClass;
	
	private AtelierCodec<T> codec;
	
	public AtelierType(Class<T> typeClass) {
		this.typeClass = typeClass;
	}
	
	public Class<T> getTypeClass() {
		return typeClass;
	}
	
	public void setFactoryClass(Class<?> factoryClass) {
		this.factoryClass = (Class<? extends Factory<T>>) factoryClass;
		//TODO sanity check that class is indeed valid factory type
	}
	
	public Class<? extends Factory<T>> getFactoryClass() {
		return factoryClass;
	}
	
	public void setCodec(AtelierCodec<T> codec) {
		this.codec = codec;
	}
	
	public AtelierCodec<T> getCodec() {
		return codec;
	}

}
