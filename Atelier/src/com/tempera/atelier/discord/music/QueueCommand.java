package com.tempera.atelier.discord.music;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.PermissionLevel;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QueueCommand extends MusicSubCommand {

	private MenuManager menuManager;

	public QueueCommand(AtelierBot bot) {
		super(bot.getAudioManager());
		menuManager = bot.getMenuManager();
	}

	@Override
	public String getLabel() {
		return "queue";
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
	public void execute(AtelierAudioHandler audioHandler, User user,
		List<String> args, MessageReceivedEvent event) {
		int page = args.size() <= 1 ? 1
			: Math.min(Integer.parseInt(args.get(1)),
				(int) Math.ceil(audioHandler.getQueue()
					.size() / 10d));

		menuManager.addMenu(event.getChannel(),
			new QueueMenu(audioHandler, menuManager, page));
	}

}
