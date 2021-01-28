package ua.isolutions.bot.commons.messages.keyboard.factory;

import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardButtonWrapper;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;

import java.util.Collection;
import java.util.Map;

public interface KeyboardFactory {

	KeyboardWrapper createKeyboard(Collection<KeyboardButtonWrapper> keys);
	KeyboardWrapper createKeyboard(Collection<KeyboardButtonWrapper> keys, int buttonsPerRow);
	KeyboardWrapper createKeyboard(Map<String, String> keys);
	KeyboardWrapper createKeyboard(Map<String, String> keys, int buttonsPerRow);
}
