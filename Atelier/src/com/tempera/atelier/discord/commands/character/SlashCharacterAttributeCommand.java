package com.tempera.atelier.discord.commands.character;

import java.util.ArrayList;
import java.util.List;

import com.tempera.atelier.discord.SlashMenuManager;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.SlashBaseSubcommand;
import com.tempera.atelier.dnd.types.character.AbstractClass;
import com.tempera.atelier.dnd.types.character.CharacterAttribute;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SlashCharacterAttributeCommand extends SlashBaseSubcommand {

	public SlashCharacterAttributeCommand() {
		super("attribute", "Gets the character's attribute information");
	}

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		if(event.getOption("attribute") != null) {
			String[] attributeInfo = event.getOption("attribute").getAsString().split(":");;
			CharacterAttribute attribute = user
					.getSheet().getClasses().stream()
					.filter(c -> c.getName().equals(attributeInfo[0]))
					.findFirst()
					
					.get().getAttributeMap().values().stream()
					.filter(a -> a.getName().equals(attributeInfo[1]))
					.findFirst().get();
			
			SlashMenuManager.getInstance().addMenu(attribute.getMenu(), true, event);
		} else {
			event.reply(new CharacterAttributeMessageBuilder(user.getSheet()).build()).setEphemeral(true).queue();
		}
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		List<String> choices = new ArrayList<>();
		for(AbstractClass c : user.getSheet().getClasses()) {
			for(CharacterAttribute a : c.getAttributeMap().values()) {
				choices.add(c.getName() + ":" + a.getName());
			}
		}
		
		event.replyChoiceStrings(choices).queue();
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, "attribute", "the name of the attribute to get", false, true);
	}

}
 