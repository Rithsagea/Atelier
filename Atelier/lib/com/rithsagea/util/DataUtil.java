package com.rithsagea.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataUtil {
	public static <T> Set<T> set(@SuppressWarnings("unchecked") T...t) {
		Set<T> set = new LinkedHashSet<>(Arrays.asList(t));
		
		return Collections.unmodifiableSet(set);
	}
	
	public static <T> List<T> list(@SuppressWarnings("unchecked") T...t) {
		return Arrays.asList(t);
	}
	
	public static <T> Map<?, ?> map(@SuppressWarnings("unchecked") T...t) {
		Map<Object, Object> map = new LinkedHashMap<>();
		for(int x = 0; x < t.length; x+=2) {
			map.put(t[x], t[x + 1]);
		}
		
		return Collections.unmodifiableMap(map);
	}
}
