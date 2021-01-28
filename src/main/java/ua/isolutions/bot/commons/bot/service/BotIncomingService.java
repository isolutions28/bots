package ua.isolutions.bot.commons.bot.service;

import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.Message;

import java.util.Collection;
import java.util.concurrent.Future;

public interface BotIncomingService {
	Future<Collection<Message>> processMessage(ActionMessage message);
}
