package ua.isolutions.bot.commons.messages.message.response;

import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;

public interface Message { // todo check if should add messenger type and user id
	String getUserId();
	MessengerType getMessengerType();
	KeyboardWrapper getKeyboard();
	void setUserId(String userId);
}
