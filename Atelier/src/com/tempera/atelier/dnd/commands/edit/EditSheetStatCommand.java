package com.tempera.atelier.dnd.commands.edit;

import java.util.List;

import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.acommands.CommandRegistry;
import com.tempera.atelier.discord.acommands.PermissionLevel;
import com.tempera.atelier.dnd.types.Sheet;
import com.tempera.atelier.dnd.types.TypeRegistry;
import com.tempera.atelier.dnd.types.enums.Ability;
import com.tempera.atelier.dnd.types.spread.AbilitySpread;
import com.tempera.atelier.dnd.types.spread.PointBuySpread;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditSheetStatCommand extends EditSheetGroupCommand {

	private class TypeStat extends EditSheetBaseCommand {

		private TypeRegistry typeRegistry;

		public TypeStat(AtelierBot bot) {
			super(bot, "type", null, PermissionLevel.ADMINISTRATOR);

			typeRegistry = bot.getDatabase().getTypeRegistry();
		}

		@Override
		public void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
			MessageBuilder builder = new MessageBuilder();
			if (args.size() < 2) {
				builder.append("Ability Spread Types:\n");
				for (String type : typeRegistry.getSubtypes(AbilitySpread.class).keySet())
					builder.append("\t" + type + "\n");
			} else {
				Class<?> clazz = typeRegistry.getSubtypes(AbilitySpread.class).get(args.get(1));
				try {
					sheet.setBaseScores((AbilitySpread) clazz.newInstance());
					builder.appendFormat("Set %s stat spread to %s", sheet.getName(), clazz.getSimpleName());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			event.getChannel().sendMessage(builder.build()).queue();
		}

	}

	private class SetStat extends EditSheetBaseCommand {

		public SetStat(AtelierBot bot) {
			super(bot, "set", null, PermissionLevel.ADMINISTRATOR);
		}

		@Override
		public void execute(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
			AbilitySpread spread = sheet.getBaseScores();

			if (spread instanceof PointBuySpread) {
				PointBuySpread s = (PointBuySpread) spread;
				Ability a = Ability.fromString(args.get(1));
				int value = Integer.parseInt(args.get(2));

				if (s.setScore(a, value)) {
					event.getChannel().sendMessage(String.format(
						"Successfully set %s to %d. There are %d points remaining.",
						a, value, s.getPoints())).queue();
				} else {
					event.getChannel().sendMessage(String.format(
						"There are not enough points to set %s to %d. There are %d points remaining.",
						a, value, s.getPoints())).queue();
				}
			}
		}

	}

	public EditSheetStatCommand(AtelierBot bot) {
		super(bot, "stat", null, PermissionLevel.ADMINISTRATOR);
		
		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new TypeStat(bot));
		reg.registerCommand(new SetStat(bot));
	}

	@Override
	public void executeDefault(User user, Sheet sheet, List<String> args, MessageReceivedEvent event) {
		AbilitySpread spread = sheet.getBaseScores();
		if (spread == null)
			event.getChannel().sendMessage("This sheet does not have an ability spread").queue();
		else
			event.getChannel().sendMessage(spread.toString()).queue();
	}

}
