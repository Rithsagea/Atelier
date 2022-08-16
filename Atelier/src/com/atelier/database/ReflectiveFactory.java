package com.atelier.database;

public class ReflectiveFactory<T> implements Factory<T> {

	private Class<T> type;
	
	public ReflectiveFactory(Class<T> type) {
		this.type = type;
	}
	
	@Override
	public T build() {
		try {
			return type.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
