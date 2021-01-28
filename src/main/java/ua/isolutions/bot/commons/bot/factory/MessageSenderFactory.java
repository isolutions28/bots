package ua.isolutions.bot.commons.bot.factory;

import ua.isolutions.bot.commons.bot.service.MessageSender;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;

public interface MessageSenderFactory {
	MessageSender getSender(MessengerType messengerType);
}
