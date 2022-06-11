package com.tempera.atelier.dnd.commands.edit;

import java.util.List;
import java.util.UUID;

import com.rithsagea.util.DataUtil;
import com.tempera.atelier.AtelierBot;
import com.tempera.atelier.discord.User;
import com.tempera.atelier.discord.lcommands.CommandRegistry;
import com.tempera.atelier.discord.lcommands.PermissionLevel;
import com.tempera.atelier.dnd.types.AtelierDB;
import com.tempera.atelier.dnd.types.Campaign;
import com.tempera.atelier.dnd.types.Sheet;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EditUserCommand extends EditUserGroupCommand {

	private class UserAddSheet extends EditUserBaseCommand {
		private AtelierDB db;
		
		public UserAddSheet(AtelierBot bot) {
			super(bot, "add", null, PermissionLevel.ADMINISTRATOR);
			db = bot.getDatabase();
		}

		@Override
		public void execute(User user, User target, List<String> args, MessageReceivedEvent event) {
			Sheet sheet = db.getSheet(UUID.fromString(args.get(2)));
			if(sheet == null) {
				event.getChannel().sendMessage("Invalid sheet id!").queue();
				return;
			}
			
			target.addSheet(sheet);
			event.getChannel().sendMessage("Added sheet to " + target.getTag() + ": " + sheet).queue();
		}
	}
	
	private AtelierDB db;
	
	public EditUserCommand(AtelierBot bot) {
		super(bot, "user", DataUtil.list("u"), PermissionLevel.ADMINISTRATOR);
		
		CommandRegistry reg = getCommandRegistry();
		reg.registerCommand(new UserAddSheet(bot));
		
		db = bot.getDatabase();
	}

	@Override
	public void executeDefault(User user, User target, List<String> args, MessageReceivedEvent event) {
		Sheet sheet = target == null ? null : db.getSheet(target.getSheetId());
		Campaign campaign = target == null ? null : db.getCampaign(target.getCampaignId());
		event.getChannel().sendMessage(
				String.format("Viewing: %s\nLevel: %s\nSheet: %s\nCampaign: %s", target, target.getLevel(), sheet, campaign)).queue();
	}
	
}
