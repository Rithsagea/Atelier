package com.atelier;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

import com.atelier.discord.AtelierUser;
import com.atelier.dnd.AtelierCharacter;

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
	
	public TemplateMessage addUser(AtelierUser user, String name) {
		tokens.put(name + ".name", user.getName());
		tokens.put(name + ".id", Long.toString(user.getId()));
		tokens.put(name, user.toString());
		return this;
	}
	
	public TemplateMessage addUser(AtelierUser user) {
		return addUser(user, "user");
	}
	
	public TemplateMessage addCharacter(AtelierCharacter character, String name) {
		tokens.put(name + ".id", character.getId().toString());
		tokens.put(name, character.toString());
		return this;
	}
	
	public TemplateMessage addCharacter(AtelierCharacter character) {
		return addCharacter(character, "character");
	}
	
	public String get() {
		return new StringSubstitutor(tokens).replace(msg);
	}
}
