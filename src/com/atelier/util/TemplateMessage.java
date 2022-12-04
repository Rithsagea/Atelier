package com.atelier.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

import com.atelier.discord.AtelierUser;
import com.atelier.dnd.Ability;
import com.atelier.dnd.Skill;
import com.atelier.dnd.campaign.Campaign;
import com.atelier.dnd.character.AtelierCharacter;

public class TemplateMessage {
	
	private String msg;
	
	private Map<String, String> tokens;
	
	public TemplateMessage(String msg) {
		this.msg = msg;
		
		tokens = new HashMap<>();
	}
	
	/**
	 * Uses Object.toString() as the value
	 */
	public TemplateMessage add(String token, Object value) {
		tokens.put(token, value.toString());
		return this;
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
		if(user == null) {
			tokens.put(token, "null");
			return this;
		}

		tokens.put(token + ".name", user.getName());
		tokens.put(token + ".id", Long.toString(user.getId()));
		tokens.put(token, user.toString());
		return this;
	}
	
	public TemplateMessage addUser(AtelierUser user) {
		return addUser("user", user);
	}
	
	public TemplateMessage addCharacter(String token, AtelierCharacter character) {
		if(character == null) {
			tokens.put(token, "null");
			return this;
		}

		tokens.put(token + ".id", character.getId().toString());
		tokens.put(token + ".name", character.getName());
		tokens.put(token, character.toString());
		return this;
	}
	
	public TemplateMessage addCharacter(AtelierCharacter character) {
		return addCharacter("character", character);
	}

	public TemplateMessage addCampaign(String token, Campaign campaign) {
		if(campaign == null) {
			tokens.put(token, "null");
			return this;
		}

		tokens.put(token + ".id", campaign.getId().toString());
		tokens.put(token + ".name", campaign.getName());
		tokens.put(token, campaign.toString());
		return this;
	}

	public TemplateMessage addCampaign(Campaign campaign) {
		return addCampaign("campaign", campaign);
	}
	
	public TemplateMessage addAbility(String token, Ability ability) {
		if(ability == null) {
			tokens.put(token, "null");
			return this;
		}

		tokens.put(token, ability.getName());
		return this;
	}
	
	public TemplateMessage addAbility(Ability ability) {
		return addAbility("ability", ability);
	}
	
	public TemplateMessage addSkill(String token, Skill skill) {
		if(skill == null) {
			tokens.put(token, "null");
			return this;
		}

		tokens.put(token, skill.getName());
		return this;
	}
	
	public TemplateMessage addSkill(Skill skill) {
		return addSkill("skill", skill);
	}
	
	public String get() {
		return new StringSubstitutor(tokens).replace(msg);
	}

	@Override
	public String toString() {
		return get();
	}
}
