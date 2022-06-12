package com.rithsagea.util;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
}
