package ua.isolutions.bot.telegram.factory;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Map;

public interface TelegramKeyboardFactory {

    ReplyKeyboard createKeyboard(KeyboardType keyboardType);
    ReplyKeyboardMarkup customKeyboard(List<String> buttonNames);
	InlineKeyboardMarkup customCallbackKeyboard(Map<String, String> buttonData);
	InlineKeyboardMarkup customCallbackKeyboard(Map<String, String> buttonData, int buttonsPerRow);

	enum KeyboardType {
        HIDE_KEY_KEYBOARD, EMPTY_KEYBOARD, INLINE_MARKUP
    }
}
