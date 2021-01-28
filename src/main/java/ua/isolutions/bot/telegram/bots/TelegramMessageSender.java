package ua.isolutions.bot.telegram.bots;

import ua.isolutions.bot.commons.bot.service.MessageSender;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;

public interface TelegramMessageSender extends MessageSender {

	default MessengerType getType() {
		return MessengerType.TELEGRAM;
	}
}
