package com.tempera.atelier.discord.music;

import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QueueCommand extends MusicSubCommand{

	private MenuManager buttons;

	public QueueCommand(AtelierBot bot) {
		super(bot.getAudioManager());
		buttons = bot.getMenuManager();
	}

	@Override
	public String getLabel() {
		return "queue";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("m", "audio");
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.USER;
	}

	@Override
	public void execute(AtelierAudioHandler audioHandler, User user, List<String> args, MessageReceivedEvent event) {
		int page = 1;
		if (args.size() > 1) {
			page = Math.min(Integer.parseInt(args.get(1)), (int)Math.ceil((double)audioHandler.getQueue().size() / 10));
		}
		
		QueueMessageBuilder msg = new QueueMessageBuilder(audioHandler, page, event.getMessage(), buttons);
		msg.send(event.getChannel());
	}

}
