package com.tempera.atelier.discord.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.dnd.User;
import com.tempera.util.NekoUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WaifuCommand extends GroupCommand {

	private class WaifuSubCommand implements AtelierCommand {

		private String pictureType;
		
		public WaifuSubCommand(String pictureType) {
			this.pictureType = pictureType;
		}
		
		@Override
		public String getLabel() {
			return pictureType;
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
			String url;
			
			switch(pictureType) {
				case "cat": url = NekoUtil.getCat(); break;
				case "dog": url = NekoUtil.getDog(); break;
				case "catboy": url = NekoUtil.getCatboy(); break;
				case "catgirl": url = NekoUtil.getCatgirl(); break;
				case "foxgirl": url = NekoUtil.getFoxgirl(); break;
				default: url = NekoUtil.getWaifu(); break;
			}
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.CYAN);
			eb.setImage(url);
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
		}
	}
	
	public WaifuCommand() {
		CommandRegistry registry = this.getCommandRegistry();
		
		registry.registerCommand(new WaifuSubCommand("cat"));
		registry.registerCommand(new WaifuSubCommand("dog"));
		registry.registerCommand(new WaifuSubCommand("catboy"));
		registry.registerCommand(new WaifuSubCommand("catgirl"));
		registry.registerCommand(new WaifuSubCommand("foxgirl"));
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
		
	}
	
}
