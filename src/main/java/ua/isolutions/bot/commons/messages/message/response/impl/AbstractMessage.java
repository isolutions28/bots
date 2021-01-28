package ua.isolutions.bot.commons.messages.message.response.impl;

import lombok.Data;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;

@Data
public abstract class AbstractMessage implements Message {
	private String userId;
	private MessengerType messengerType;
	private KeyboardWrapper keyboard;
}
