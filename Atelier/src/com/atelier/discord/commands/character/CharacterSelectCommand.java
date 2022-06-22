package com.atelier.discord.commands.character;

import java.util.UUID;
import java.util.stream.Collectors;

import com.atelier.AtelierLanguageManager;
import com.atelier.discord.User;
import com.atelier.discord.commands.BaseInteraction.BaseSubcommand;
import com.atelier.dnd.types.AtelierDB;
import com.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CharacterSelectCommand extends BaseSubcommand {

	private final String optionIdName = AtelierLanguageManager.getInstance().get(this, "id.name");
	private final String optionIdDescription = AtelierLanguageManager.getInstance().get(this, "id.description");
	
	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		Sheet sheet = AtelierDB.getInstance().getSheet(UUID.fromString(event.getOption("id").getAsString()));
		user.setSheet(sheet);
		
		event.reply("Selected sheet: " + sheet).setEphemeral(true).queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		event.replyChoiceStrings(user.getSheets().stream()
				.map(s -> s.getId().toString())
				.collect(Collectors.toList())).queue();
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, optionIdName, optionIdDescription, true, true);
	}

}
