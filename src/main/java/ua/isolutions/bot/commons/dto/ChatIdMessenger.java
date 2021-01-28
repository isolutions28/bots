package ua.isolutions.bot.commons.dto;

import lombok.Data;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;

@Data
public class ChatIdMessenger {
	private final String chatId;
	private final MessengerType messengerType;
}
