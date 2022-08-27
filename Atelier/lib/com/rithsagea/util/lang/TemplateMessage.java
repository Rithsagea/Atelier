package com.rithsagea.util.lang;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

public class TemplateMessage {
	
	private String msg;
	
	private Map<String, String> tokens;
	
	public TemplateMessage(String msg) {
		this.msg = msg;
		
		tokens = new HashMap<>();
	}
	
	public TemplateMessage add(String token, String value) {
		tokens.put(token, value);
		return this;
	}
	
	public String get() {
		return new StringSubstitutor(tokens).replace(msg);
	}
}
