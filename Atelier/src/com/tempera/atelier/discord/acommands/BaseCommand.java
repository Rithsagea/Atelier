package com.tempera.atelier.discord.acommands;

import java.util.List;

public abstract class BaseCommand implements AtelierCommand {

	private String label;
	private List<String> aliases;
	private PermissionLevel level;

	public BaseCommand(String label, List<String> aliases, PermissionLevel level) {
		this.label = label;
		this.aliases = aliases;
		this.level = level;
	}

	@Override
	public final String getLabel() {
		return label;
	}

	@Override
	public final List<String> getAliases() {
		return aliases;
	}

	@Override
	public final PermissionLevel getLevel() {
		return level;
	}
}
