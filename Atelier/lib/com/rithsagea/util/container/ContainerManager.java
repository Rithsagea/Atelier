package com.rithsagea.util.container;

import java.util.HashMap;
import java.util.Map;

public class ContainerManager {
	
	private Map<String, Container> containers;
	
	public ContainerManager() {
		containers = new HashMap<>();
	}
	
	public Container getContainer(String id) {
		return containers.get(id);
	}
	
	public Container addContainer(String id, Container container) {
		return containers.put(id, container);
	}
	
	public Container removeContainer(String id) {
		return containers.remove(id);
	}
}
