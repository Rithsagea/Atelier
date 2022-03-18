package com.rithsagea.atelier.discord;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.rithsagea.atelier.dnd.User;
import com.rithsagea.util.NekoUtil;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WaifuCommand implements AtelierCommand {

	@Override
	public String getLabel() {
		return "waifu";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("cat", "dog", "catboy", "catgirl", "foxgirl");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(User user, List<String> args, MessageReceivedEvent event) {
		String url;
		
		switch(args.get(0)) {
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
