package com.atelier.dnd.types.alpha;

public class DefaultFactory<T> implements Factory<T> {

	private Class<T> clazz;
	
	public DefaultFactory(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public T build() {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<T> getType() {
		return clazz;
	}
}
