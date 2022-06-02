package com.rithsagea.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.rithsagea.util.DataUtil;

public class EventBus {
	private static class Handler implements Comparable<Handler> {
		public int priority;
		public Method method;
		public Listener listener;

		public Handler(int priority, Method method, Listener listener) {
			this.priority = priority;
			this.method = method;
			this.listener = listener;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Handler && method == ((Handler) obj).method
				&& listener == ((Handler) obj).listener;
		}

		@Override
		public int compareTo(Handler h) {
			return h.priority - priority;
		}
	}

	private Map<Class<?>, Set<Handler>> listenerMap;

	public EventBus() {
		listenerMap = new HashMap<>();
	}

	public void registerListener(Listener listener) {
		for (Method method : DataUtil.getMethods(listener.getClass())) {
			if (method.getParameterCount() == 1) {
				Parameter p = method.getParameters()[0];
				EventHandler ann = method.getAnnotation(EventHandler.class);
				Class<?> type = p.getType();
				if (ann != null) {
					if (!Event.class.isAssignableFrom(type))
						continue;
					method.setAccessible(true);
					Handler handler = new Handler(ann.priority(), method,
						listener);

					if (!listenerMap.containsKey(type))
						listenerMap.put(type, new TreeSet<>());
					listenerMap.get(type)
						.add(handler);
				}
			}
		}
	}

	public void unregisterListener(Listener listener) {
		if (listener == null)
			return;
		for (Method method : DataUtil.getMethods(listener.getClass())) {
			if (method.getParameterCount() == 1) {
				Parameter p = method.getParameters()[0];
				Class<?> type = p.getType();
				if (Event.class.isAssignableFrom(type)
					&& method.isAnnotationPresent(EventHandler.class)) {
					if (listenerMap.containsKey(type)) {
						listenerMap.get(type)
							.removeIf((h) -> h.listener == listener);
					}
				}
			}
		}
	}

	public void clearListeners() {
		listenerMap.clear();
	}

	public void submitEvent(Event event) {
		Class<?> type = event.getClass();
		while (Event.class.isAssignableFrom(type)) {
			if (listenerMap.containsKey(type)) {
				for (Handler handler : listenerMap.get(type)) {
					try {
						handler.method.invoke(handler.listener, event);
					} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}

			type = type.getSuperclass();
		}
	}
}
