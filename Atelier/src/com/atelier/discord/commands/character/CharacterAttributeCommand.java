package com.atelier.discord.commands.character;

import java.util.ArrayList;
import java.util.List;

import com.atelier.AtelierLanguageManager;
import com.atelier.discord.MenuManager;
import com.atelier.discord.User;
import com.atelier.discord.commands.BaseInteraction.BaseSubcommand;
import com.atelier.dnd.types.character.AbstractClass;
import com.atelier.dnd.types.character.CharacterAttribute;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CharacterAttributeCommand extends BaseSubcommand {

	private String optionAttributeName = AtelierLanguageManager.getInstance().get(this, "attribute.name");
	private String optionAttributeDescription = AtelierLanguageManager.getInstance().get(this, "attribute.description");
	
	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		if(event.getOption("attribute") != null) {
			String[] attributeInfo = event.getOption("attribute").getAsString().split(":");;
			CharacterAttribute attribute = user
					.getSheet().getClasses().stream()
					.filter(c -> c.getName().equals(attributeInfo[0]))
					.findFirst()
					
					.get().getFeatures().values().stream()
					.filter(a -> a.getName().equals(attributeInfo[1]))
					.findFirst().get();
			
			MenuManager.getInstance().addMenu(attribute.getMenu(), true, event);
		} else {
			Message message = new CharacterAttributeMessageBuilder(user.getSheet()).build();
			event.deferReply(true).queue(hook -> hook.editOriginal(message).queue());
		}
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		List<String> choices = new ArrayList<>();
		for(AbstractClass c : user.getSheet().getClasses()) {
			for(CharacterAttribute a : c.getFeatures().values()) {
				choices.add(c.getName() + ":" + a.getName());
			}
		}
		
		event.replyChoiceStrings(choices).queue();
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, optionAttributeName, optionAttributeDescription, false, true);
	}

}
 