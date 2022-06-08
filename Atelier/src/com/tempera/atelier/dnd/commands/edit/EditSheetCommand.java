package com.tempera.atelier.dnd.commands.edit;

import java.util.List;
import java.util.UUID;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.BaseCommand;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditSheetCommand extends EditSheetGroupCommand {

	private class ListSheet extends BaseCommand {

		private AtelierDB db;

		public ListSheet(AtelierBot bot) {
			super("list", null, PermissionLevel.ADMINISTRATOR);
			db = bot.getDatabase();
		}

		@Override
		public void execute(User user, List<String> args, MessageReceivedEvent event) {
			MessageBuilder b = new MessageBuilder();
			for (Sheet sheet : db.listSheets()) {
				b.append(sheet);
				b.append("\n");
			}

			event.getChannel().sendMessage(b.build()).queue();
		}

	}

	private class NameSheet extends EditSheetBaseCommand {
		public NameSheet(AtelierBot bot) {
			super(bot, "name", null, PermissionLevel.ADMINISTRATOR);
		}

		@Override
		public void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
			if (args.size() >= 2) {
				sheet.setName(args.get(1));
				event.getChannel().sendMessage("Set character's name to " + args.get(1)).queue();
				return;
			}

			event.getChannel().sendMessage("Character name: " + sheet.getName()).queue();
		}
	}

	private class NewSheet extends BaseCommand {

		private AtelierDB db;

		public NewSheet(AtelierBot bot) {
			super("new", null, PermissionLevel.ADMINISTRATOR);
			db = bot.getDatabase();
		}

		@Override
		public void execute(User user, List<String> args, MessageReceivedEvent event) {
			Sheet sheet = new Sheet();
			db.addSheet(sheet);

			event.getChannel().sendMessageFormat("Created new sheet %s", sheet.getId()).queue();
		}
	}

	private class SelectSheet extends BaseCommand {
		private AtelierDB db;

		public SelectSheet(AtelierBot bot) {
			super("select", null, PermissionLevel.ADMINISTRATOR);
			db = bot.getDatabase();
		}

		@Override
		public void execute(User user, List<String> args, MessageReceivedEvent event) {
			if (args.size() < 2) {
				event.getChannel().sendMessage("Specify a valid sheet id").queue();
				return;
			}

			UUID id;

			try {
				id = UUID.fromString(args.get(1));
			} catch (IllegalArgumentException e) {
				event.getChannel().sendMessage("Invalid id format").queue();
				return;
			}

			Sheet sheet = db.getSheet(id);
			if (sheet != null) {
				user.setSelectedSheet(sheet);
				event.getChannel().sendMessage("Selected sheet: " + sheet).queue();
				return;
			}
		}
	}

	public EditSheetCommand(AtelierBot bot) {
		super(bot, "sheet", DataUtil.list("s"), PermissionLevel.ADMINISTRATOR);

		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new ListSheet(bot));
		reg.registerCommand(new NameSheet(bot));
		reg.registerCommand(new NewSheet(bot));
		reg.registerCommand(new SelectSheet(bot));

		reg.registerCommand(new EditSheetStatCommand(bot));
	}

	@Override
	public void executeDefault(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
		event.getChannel().sendMessage("Currently editing: " + sheet).queue();
	}

}
