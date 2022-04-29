package com.tempera.atelier.discord.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.tempera.atelier.dnd.User;
import com.tempera.util.NekoUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WaifuCommand extends GroupCommand {

	private class WaifuSubCommand implements AtelierCommand {

		private String label;
		private Supplier<String> urlSupplier;
		
		public WaifuSubCommand(String label, Supplier<String> urlSupplier) {
			this.label = label;
			this.urlSupplier = urlSupplier;
		}
		
		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public List<String> getAliases() {
			return null;
		}

		@Override
		public PermissionLevel getLevel() {
			return PermissionLevel.USER;
		}

		@Override
		public void execute(User user, List<String> args, MessageReceivedEvent event) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.CYAN);
			eb.setImage(urlSupplier.get());
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
		}
	}
	
	public WaifuCommand() {
		CommandRegistry registry = this.getCommandRegistry();
		
		registry.registerCommand(new WaifuSubCommand("cat", NekoUtil::getCat));
		registry.registerCommand(new WaifuSubCommand("dog", NekoUtil::getDog));
		registry.registerCommand(new WaifuSubCommand("catboy", NekoUtil::getCatboy));
		registry.registerCommand(new WaifuSubCommand("catgirl", NekoUtil::getCatgirl));
		registry.registerCommand(new WaifuSubCommand("foxgirl", NekoUtil::getFoxgirl));
	}
	
	@Override
	public String getLabel() {
		return "waifu";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("w");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void executeDefault(User user, List<String> args, MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.CYAN);
		eb.setImage(NekoUtil.getWaifu());
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
	
}
