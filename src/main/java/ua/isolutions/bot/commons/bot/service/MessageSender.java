package ua.isolutions.bot.commons.bot.service;

import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;

public interface MessageSender {

	<T extends Message> boolean sendMessage(T message);
	MessengerType getType();
}
