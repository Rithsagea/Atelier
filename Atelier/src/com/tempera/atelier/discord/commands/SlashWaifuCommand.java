package com.tempera.atelier.discord.commands;

import com.tempera.atelier.discord.SlashBaseCommand;
import com.tempera.atelier.discord.User;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashWaifuCommand extends SlashBaseCommand {

	public SlashWaifuCommand() {
		super("waifu", "Provides waifus for the degenerate masses");
	}

	@Override
	public void execute(User user, SlashCommandInteractionEvent event) {
		event.reply("Here is waifu").queue();
	}

}
