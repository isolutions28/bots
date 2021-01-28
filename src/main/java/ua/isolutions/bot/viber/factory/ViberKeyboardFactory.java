package ua.isolutions.bot.viber.factory;

import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.MessageKeyboardButton;

import java.util.Collection;
import java.util.Map;

public interface ViberKeyboardFactory {

	MessageKeyboard createKeyboard(Collection<MessageKeyboardButton> keys);

	MessageKeyboard createKeyboard(Map<String, String> keys);

	MessageKeyboard createKeyboard(Map<String, String> keys, int buttonsPerRow);
}
