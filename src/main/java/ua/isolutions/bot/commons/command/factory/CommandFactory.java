package ua.isolutions.bot.commons.command.factory;

import ua.isolutions.bot.commons.command.Command;

public interface CommandFactory {
	Command getCommand(String text);
}
