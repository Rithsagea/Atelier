package com.rithsagea.util.data;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataUtil {
	public static <T> Set<T> set(@SuppressWarnings("unchecked") T... t) {
		Set<T> set = new LinkedHashSet<>(Arrays.asList(t));

		return Collections.unmodifiableSet(set);
	}

	public static <T> List<T> list(@SuppressWarnings("unchecked") T... t) {
		return Arrays.asList(t);
	}

	public static <T> Map<?, ?> map(@SuppressWarnings("unchecked") T... t) {
		Map<Object, Object> map = new LinkedHashMap<>();
		for (int x = 0; x < t.length; x += 2) {
			map.put(t[x], t[x + 1]);
		}

		return Collections.unmodifiableMap(map);
	}

	// https://stackoverflow.com/a/28408148
	public static Set<Method> getMethods(Class<?> clazz) {
		Set<Method> methods = new LinkedHashSet<>();
		Collections.addAll(methods, clazz.getMethods());
		Map<Object, Set<Package>> types = new HashMap<>();
		final Set<Package> pkgIndependent = Collections.emptySet();
		for (Method m : methods)
			types.put(methodKey(m), pkgIndependent);

		final int access = Modifier.PUBLIC | Modifier.PROTECTED
			| Modifier.PRIVATE;
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			for (Method m : c.getDeclaredMethods()) {
				final int mod = m.getModifiers();
				if (!Modifier.isStatic(mod))
					switch (mod & access) {

					case Modifier.PUBLIC:
						continue;
					default:
						Set<Package> pkg = types.computeIfAbsent(methodKey(m),
							key -> new HashSet<>());
						if (pkg != pkgIndependent && pkg.add(c.getPackage()))
							break;
						else
							continue;
					case Modifier.PROTECTED:
						if (types.putIfAbsent(methodKey(m),
							pkgIndependent) != null)
							continue;
					case Modifier.PRIVATE:

					}
				methods.add(m);
			}
		}

		return methods;
	}

	public static Set<Field> getFields(Class<?> clazz) {
		Map<String, Field> fields = new TreeMap<>();
		
		for(Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			Stream.of(c.getDeclaredFields())
				.filter(field -> !Modifier.isStatic(field.getModifiers()))
				.filter(field -> !fields.containsKey(field.getName()))
				.forEach(field -> fields.put(field.getName(), field));
		}
		
		return new LinkedHashSet<>(fields.values());
	}
	
	private static Object methodKey(Method m) {
		return Arrays.asList(m.getName(),
			MethodType.methodType(m.getReturnType(), m.getParameterTypes()));
	}

	public static Object getField(Object object, String fieldName) {
		try {
			Field field = null;
			Class<?> clazz = object.getClass();
			while(field == null) {
				try {
					field = clazz.getDeclaredField(fieldName);
					break;
				} catch(NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				}
			}
			
			field.setAccessible(true);
			return field.get(object);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static boolean isNoneType(Object obj) {
		if(obj == null) return true;
		
		if(obj instanceof int[]) return ((int[]) obj).length == 0;
		if(obj instanceof int[][]) return ((int[][]) obj).length == 0;
		if(obj instanceof double[]) return ((double[]) obj).length == 0;
		if(obj instanceof double[][]) return ((double[][]) obj).length == 0;
		if(obj instanceof short[]) return ((short[]) obj).length == 0;
		if(obj instanceof byte[]) return ((byte[]) obj).length == 0;
		if(obj instanceof boolean[]) return ((boolean[]) obj).length == 0;
		if(obj instanceof Object[]) return ((Object[]) obj).length == 0;
		
		if(obj instanceof Collection) return ((Collection<?>) obj).isEmpty();
		if(obj instanceof Map) return ((Map<?, ?>) obj).isEmpty();
		
		return false;
	}

	// https://stackoverflow.com/a/1704658
	private static final Map<Class<?>, Class<?>> WRAPPER_MAP;
	private static final Map<Class<?>, Class<?>> PRIMITIVE_MAP;
	static {
		WRAPPER_MAP = new HashMap<>();
		WRAPPER_MAP.put(boolean.class, Boolean.class);
		WRAPPER_MAP.put(byte.class, Byte.class);
		WRAPPER_MAP.put(char.class, Character.class);
		WRAPPER_MAP.put(double.class, Double.class);
		WRAPPER_MAP.put(float.class, Float.class);
		WRAPPER_MAP.put(int.class, Integer.class);
		WRAPPER_MAP.put(long.class, Long.class);
		WRAPPER_MAP.put(short.class, Short.class);
		WRAPPER_MAP.put(void.class, Void.class);
		
		PRIMITIVE_MAP = WRAPPER_MAP.entrySet()
			.stream()
			.collect(Collectors.toMap(Entry::getValue, Entry::getKey));
	}
	
	public static Class<?> getWrapper(Class<?> primitiveClass) {
		return WRAPPER_MAP.get(primitiveClass);
	}
	
	public static Class<?> getPrimitive(Class<?> wrapperClass) {
		return PRIMITIVE_MAP.get(wrapperClass);
	}
}
