package com.tempera.atelier.discord.commands.character;

import java.util.UUID;
import java.util.stream.Collectors;

import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.SlashBaseSubcommand;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SlashCharacterSelectCommand extends SlashBaseSubcommand {

	public SlashCharacterSelectCommand() {
		super("select", "Selects a character sheet");
	}

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		Sheet sheet = AtelierDB.getInstance().getSheet(UUID.fromString(event.getOption("id").getAsString()));
		user.setSheet(sheet);
		
		event.reply("Selected sheet: " + sheet).setEphemeral(true).queue();
	}

	@Override
	public void complete(User user, CommandAutoCompleteInteractionEvent event) {
		event.replyChoiceStrings(user
				.getSheets()
				.stream()
				.map(s -> s.getId().toString())
				.collect(Collectors.toList())).queue();
	}
	
	@Override
	public void addOptions(SubcommandData data) {
		data.addOption(OptionType.STRING, "id", "the id of the character sheet", true, true);
	}

}
