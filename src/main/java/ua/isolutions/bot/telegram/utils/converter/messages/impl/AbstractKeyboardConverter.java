package ua.isolutions.bot.telegram.utils.converter.messages.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.factory.TelegramKeyboardFactory;

import java.util.Map;

import static java.util.Objects.isNull;
import static ua.isolutions.bot.telegram.factory.TelegramKeyboardFactory.KeyboardType.HIDE_KEY_KEYBOARD;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ConditionalOnBean(TelegramBotConfig.class)
public abstract class AbstractKeyboardConverter {
	private TelegramKeyboardFactory telegramKeyboardFactory;

	protected ReplyKeyboard createKeyboard(Message message) {
		KeyboardWrapper keyboard = message.getKeyboard();
		if (isNull(keyboard)) {
			return telegramKeyboardFactory.createKeyboard(HIDE_KEY_KEYBOARD);
		}
		Map<String, String> buttons = keyboard.getButtonsAsMap();
		if (isNull(buttons)) {
			return telegramKeyboardFactory.createKeyboard(HIDE_KEY_KEYBOARD);
		}

		return telegramKeyboardFactory.customCallbackKeyboard(buttons, keyboard.getButtonsPerRow());
	}

	@Autowired
	public void setTelegramKeyboardFactory(TelegramKeyboardFactory telegramKeyboardFactory) {
		this.telegramKeyboardFactory = telegramKeyboardFactory;
	}
}
