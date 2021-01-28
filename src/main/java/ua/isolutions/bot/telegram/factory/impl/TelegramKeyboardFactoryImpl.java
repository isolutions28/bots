package ua.isolutions.bot.telegram.factory.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.isolutions.bot.commons.exceptions.NoSuchKeyboardException;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.factory.TelegramKeyboardFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
@ConditionalOnBean(TelegramBotConfig.class)
public class TelegramKeyboardFactoryImpl implements TelegramKeyboardFactory {

	@Override
	public ReplyKeyboard createKeyboard(KeyboardType keyboardType) {
		ReplyKeyboard replyKeyboard;
		try {
			switch (keyboardType) {
				case HIDE_KEY_KEYBOARD:
					replyKeyboard = hideKeyboard();
					break;
				case EMPTY_KEYBOARD:
					replyKeyboard = emptyKeyboard();
					break;
				case INLINE_MARKUP:
					replyKeyboard = inlineMarkup();
					break;
				default:
					throw new NoSuchKeyboardException(keyboardType.name());
			}
			log.debug("Created keyboard: " + keyboardType);
			return replyKeyboard;
		} catch (NoSuchKeyboardException e) {
			log.error("Unable to create keyboard: " + keyboardType);
			throw e;
		}
	}

	@Override
	public ReplyKeyboardMarkup customKeyboard(List<String> buttonNames) {
		ReplyKeyboardMarkup replyKeyboardMarkup =
				(ReplyKeyboardMarkup) createKeyboard(KeyboardType.EMPTY_KEYBOARD);
		replyKeyboardMarkup.setKeyboard(buttonList(buttonNames));
		return replyKeyboardMarkup;
	}

	private ReplyKeyboardRemove hideKeyboard() {
		return new ReplyKeyboardRemove(true);
	}

	private ReplyKeyboard inlineMarkup() {
		return new InlineKeyboardMarkup();
	}

	private ReplyKeyboardMarkup emptyKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(true);
		return replyKeyboardMarkup;
	}

	private List<KeyboardRow> buttonList(List<String> buttonNames) {
		List<KeyboardRow> replyKeyboard = new ArrayList<>();

		for (String buttonName : buttonNames) {
			replyKeyboard.add(oneButtonRow(buttonName));
		}

		return replyKeyboard;
	}

	private KeyboardRow oneButtonRow(String buttonText) {
		return buttonsRow(Collections.singletonList(buttonText));
	}

	private KeyboardRow buttonsRow(List<String> buttonTextList) {
		KeyboardRow keyboardButtons = keyboardRow();
		for (String buttonText : buttonTextList) {
			keyboardButtons.add(keyboardButton(buttonText));
		}
		return keyboardButtons;
	}

	private KeyboardButton keyboardButton(String buttonText) {
		KeyboardButton button = new KeyboardButton();
		button.setText(buttonText);
		return button;
	}

	private KeyboardRow keyboardRow() {
		return new KeyboardRow();
	}

	/**
	 * Method creates inline keyboard markup with buttons from map
	 *
	 * @param buttonData buttons data, where key is button text and value is callback data
	 * @return inline keyboard markup with buttons
	 */
	@Override
	public InlineKeyboardMarkup customCallbackKeyboard(Map<String, String> buttonData) {
		return createCallBackButtonList(buttonData, 1);
	}

	@Override
	public InlineKeyboardMarkup customCallbackKeyboard(Map<String, String> buttonData, int buttonsPerRow) {
		return createCallBackButtonList(buttonData, buttonsPerRow);
	}

	private InlineKeyboardMarkup createCallBackButtonList(Map<String, String> buttonData, int buttonsPerRow) {
		InlineKeyboardMarkup inlineKeyboardMarkup =
				(InlineKeyboardMarkup) createKeyboard(KeyboardType.INLINE_MARKUP);
		inlineKeyboardMarkup.setKeyboard(callBackButtonList(buttonData, buttonsPerRow));
		return inlineKeyboardMarkup;
	}


	private List<List<InlineKeyboardButton>> callBackButtonList(Map<String, String> buttons, int buttonsPerRow) {
		List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
		List<InlineKeyboardButton> keyButtonsList = new ArrayList<>();
		for (Map.Entry<String, String> buttonData : buttons.entrySet()) {
			keyButtonsList.add(keyboardButton(buttonData.getKey(), buttonData.getValue()));
		}
		for (int i = 0; i < keyButtonsList.size(); i = i + buttonsPerRow) {
			ArrayList<InlineKeyboardButton> buttonRow = new ArrayList<>(buttonsPerRow);
			for (int j = 0; j < buttonsPerRow; j++) {
				if (i + j < keyButtonsList.size()) {
					buttonRow.add(keyButtonsList.get(i + j));
				}
			}
			inlineKeyboardButtons.add(buttonRow);
		}

		return inlineKeyboardButtons;
	}

	private InlineKeyboardButton keyboardButton(String text, String callbackData) {
		InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(text);
		inlineKeyboardButton.setCallbackData(callbackData);
		return inlineKeyboardButton;
	}
}
