package com.tempera.atelier.discord.music;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.MenuManager;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.User;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

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
		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder bt = new MessageBuilder();
		eb.setColor(Color.CYAN);
		StringBuilder msg = new StringBuilder();
		int queueSize = audioHandler.getQueue().size();
		int pageMax = (int)Math.ceil((double)queueSize / 10);
		int page = 1;

		if (args.size() > 1) {
			page = Math.min(Integer.parseInt(args.get(1)), pageMax);
		}

		for (int i = (page-1)*10; i < queueSize && i < page*10; i++) {
			msg.append(String.format("`[%d]` - **%s**\n",
					i + 1, 
					audioHandler.getQueue().get(i).getInfo().title));
		}

		if (msg.length() == 0) {
			eb.setTitle("No songs in queue!");
			bt.setActionRows(ActionRow.of(Button.success("asdf", "bad time")));
			
			bt.setEmbeds(eb.build());
			event.getChannel().sendMessage(bt.build()).queue();
		}
		else {
			eb.setTitle(queueSize + (queueSize > 1 ?" songs" :" song"));
			eb.addField("Queue:", msg.toString(), true);
			eb.appendDescription(String.format("Page %d of %d", page, pageMax));
			eb.setFooter(String.format("Displaying songs %d to %d out of %d", (page-1)*10+1,
					Math.min(page*10, queueSize), queueSize));
			
			bt.setEmbeds(eb.build());
			int passPage = page;
			event.getChannel().sendMessage(bt.build()).queue((Message message) -> {
				buttons.addMenu(message.getIdLong(), new QButton(message, passPage));
			});
		}
	}

}
