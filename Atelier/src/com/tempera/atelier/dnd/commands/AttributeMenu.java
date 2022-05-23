package com.tempera.atelier.dnd.commands;

import com.tempera.atelier.discord.Menu;
import com.tempera.atelier.dnd.Sheet;
import com.tempera.atelier.dnd.types.character.Attribute;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.enums.Skill;
import com.tempera.util.WordUtil;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu.Builder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class AttributeMenu extends Menu{

	private String selected = "";
	private Sheet sheet;
	public AttributeMenu(MessageChannel channel, Sheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public void onButtonInteract(ButtonInteractionEvent event) {
		event.reply(selected).queue();
		
	}

	@Override
	public void onSelectInteract(SelectMenuInteractionEvent event) {
		selected = event.getSelectedOptions().get(0).getValue();
		event.deferEdit().queue();
	}

	@Override
	public MessageAction initialize(MessageChannel channel) {
		MessageAction res = channel.sendMessage("Choose an attribute");
		Builder menu = SelectMenu.create("menu:roll")
				.setPlaceholder("Choose attribute")
				.setRequiredRange(1, 1);
				for(Attribute a : sheet.getClasses().get(0).getAttributes())
					menu.addOption(WordUtil.capitalize(a.getName().replace("_", " ")), a.getName());
				res.setActionRows(
						ActionRow.of(menu.build()),
						ActionRow.of(Button.primary("get", "Get")));
		return res;
	}
	

}
