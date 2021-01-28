package ua.isolutions.bot.commons.actions;

import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;

public interface SkipableAction {
	boolean skip(ActionMessage message);
}
