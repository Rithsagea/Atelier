package com.atelier.test;

import com.atelier.discord.AtelierUser;
import com.atelier.dnd.character.AtelierCharacter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperTest {
	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		AtelierUser user = new AtelierUser(420l);
		AtelierCharacter character = new AtelierCharacter();
		user.setName("Mear");
		user.addCharacter(character);
		
		character.setName("Mear");
		
		try {
			JsonNode json;
			
			json = mapper.valueToTree(user);
			System.out.println(json);
			
			AtelierUser clone = mapper.readerFor(AtelierUser.class).readValue(json);
			System.out.println("ID: " + clone.getId());
			System.out.println("Name: " + clone.getName());
			
			json = mapper.valueToTree(character);
			System.out.println(json);
			
			AtelierCharacter cloneChar = mapper.readerFor(AtelierCharacter.class).readValue(json);
			System.out.println("ID: " + cloneChar.getId());
			System.out.println("Name: " + cloneChar.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
