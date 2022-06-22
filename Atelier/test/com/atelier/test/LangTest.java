package com.atelier.test;

import com.atelier.discord.commands.AbstractInteraction;
import com.atelier.discord.commands.character.CharacterAttributeCommand;

public class LangTest {
	public static void main(String[] args) {
		AbstractInteraction cmd = new CharacterAttributeCommand();
		System.out.println("Name: " + cmd.getName() + "\nDescription: " + cmd.getDescription());
	}
}
