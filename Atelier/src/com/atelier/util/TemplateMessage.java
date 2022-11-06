package com.atelier.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

import com.atelier.discord.AtelierUser;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.character.AtelierCharacter;

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
	
	public TemplateMessage add(String token, int value) {
		tokens.put(token, Integer.toString(value));
		return this;
	}
	
	public TemplateMessage addUser(String token, AtelierUser user) {
		tokens.put(token + ".name", user.getName());
		tokens.put(token + ".id", Long.toString(user.getId()));
		tokens.put(token, user.toString());
		return this;
	}
	
	public TemplateMessage addUser(AtelierUser user) {
		return addUser("user", user);
	}
	
	public TemplateMessage addCharacter(String token, AtelierCharacter character) {
		tokens.put(token + ".id", character.getId().toString());
		tokens.put(token + ".name", character.getName());
		tokens.put(token, character.toString());
		return this;
	}
	
	public TemplateMessage addCharacter(AtelierCharacter character) {
		return addCharacter("character", character);
	}
	
	public TemplateMessage addAbility(String token, Ability ability) {
		tokens.put(token, ability.getName());
		return this;
	}
	
	public TemplateMessage addAbility(Ability ability) {
		return addAbility("ability", ability);
	}
	
	public TemplateMessage addSkill(String token, Skill skill) {
		tokens.put(token, skill.getName());
		return this;
	}
	
	public TemplateMessage addSkill(Skill skill) {
		return addSkill("skill", skill);
	}
	
	public String get() {
		return new StringSubstitutor(tokens).replace(msg);
	}
}
