package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.commands.BaseCommand;
import com.tempera.atelier.discord.commands.CommandRegistry;
import com.tempera.atelier.discord.commands.GroupCommand;
import com.tempera.atelier.discord.commands.PermissionLevel;
import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.TypeRegistry;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.spread.AbilitySpread;
import com.tempera.atelier.dnd.types.spread.PointBuySpread;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditStatCommand extends GroupCommand {

	private class TypeStat extends BaseCommand {

		private TypeRegistry typeRegistry;

		public TypeStat(AtelierBot bot) {
			super("type", null, PermissionLevel.ADMINISTRATOR);

			typeRegistry = bot.getDatabase()
				.getTypeRegistry();
		}

		@Override
		public void execute(User user, List<String> args,
			MessageReceivedEvent event) {
			MessageBuilder builder = new MessageBuilder();
			if (args.size() < 2) {
				builder.append("Ability Spread Types:\n");
				for (String type : typeRegistry.getSubtypes(AbilitySpread.class)
					.keySet()) {
					builder.append("\t" + type + "\n");
				}
			} else {
				Class<?> clazz = typeRegistry.getSubtypes(AbilitySpread.class)
					.get(args.get(1));
				try {
					user.getSelectedSheet()
						.setBaseScores((AbilitySpread) clazz.newInstance());
					builder.appendFormat("Set %s stat spread to %s",
						user.getSelectedSheet()
							.getName(),
						clazz.getSimpleName());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			event.getChannel()
				.sendMessage(builder.build())
				.queue();
		}

	}

	private class SetStat extends BaseCommand {

		public SetStat() {
			super("set", null, PermissionLevel.ADMINISTRATOR);
		}

		@Override
		public void execute(User user, List<String> args,
			MessageReceivedEvent event) {
			AbilitySpread spread = user.getSelectedSheet()
				.getBaseScores();

			if (spread instanceof PointBuySpread) {
				PointBuySpread s = (PointBuySpread) spread;
				Ability a = Ability.fromLabel(args.get(1));
				int value = Integer.parseInt(args.get(2));

				if (s.setScore(a, value)) {
					event.getChannel()
						.sendMessage(String.format(
							"Successfully set %s to %d. There are %d points remaining.",
							a, value, s.getPoints()))
						.queue();
				} else {
					event.getChannel()
						.sendMessage(String.format(
							"There are not enough points to set %s to %d. There are %d points remaining.",
							a, value, s.getPoints()))
						.queue();
				}
				return;
			}
		}

	}

	public EditStatCommand(AtelierBot bot) {
		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new TypeStat(bot));
		reg.registerCommand(new SetStat());
	}

	@Override
	public String getLabel() {
		return "stat";
	}

	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public PermissionLevel getLevel() {
		return PermissionLevel.ADMINISTRATOR;
	}

	@Override
	public void executeDefault(User user, List<String> args,
		MessageReceivedEvent event) {
		Sheet sheet = user.getSelectedSheet();

		AbilitySpread spread = sheet.getBaseScores();
		if (spread == null) {
			event.getChannel()
				.sendMessage("This sheet does not have an ability spread")
				.queue();
			return;
		}

		event.getChannel()
			.sendMessage(spread.toString())
			.queue();
	}

}
