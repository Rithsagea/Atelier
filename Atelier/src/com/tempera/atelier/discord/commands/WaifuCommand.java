package com.tempera.atelier.discord.commands;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.User;
import com.tempera.util.EmbedUtil;
import com.tempera.util.NekoUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WaifuCommand extends GroupCommand {

	private class WaifuSubCommand implements AtelierCommand {

		private String label;
		private String title;
		private Supplier<String> urlSupplier;
		
		public WaifuSubCommand(String label, String title, Supplier<String> urlSupplier) {
			this.label = label;
			this.title = title;
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
			event.getChannel().sendMessageEmbeds(EmbedUtil.getEmbed(urlSupplier.get(), title)).queue();
		}
	}
	
	private MenuManager menuManager;
	
	public WaifuCommand(AtelierBot bot) {
		CommandRegistry registry = this.getCommandRegistry();
		
		registry.registerCommand(new WaifuSubCommand("cat", "Cat", NekoUtil::getCat));
		registry.registerCommand(new WaifuSubCommand("dog", "Dog", NekoUtil::getDog));
		registry.registerCommand(new WaifuSubCommand("catboy", "Catboy", NekoUtil::getCatboy));
		registry.registerCommand(new WaifuSubCommand("catgirl", "Catgirl", NekoUtil::getCatgirl));
		registry.registerCommand(new WaifuSubCommand("foxgirl", "Foxgirl", NekoUtil::getFoxgirl));
		
		menuManager = bot.getMenuManager();
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
		menuManager.addMenu(event.getChannel(), new WaifuMenu());
	}
	
}
