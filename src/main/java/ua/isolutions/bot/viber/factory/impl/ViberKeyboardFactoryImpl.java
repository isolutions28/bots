package ua.isolutions.bot.viber.factory.impl;

import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.MessageKeyboardButton;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.factory.ViberKeyboardFactory;

import java.util.*;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Log4j2
@Component
@ConditionalOnBean(ViberConfig.class)
public class ViberKeyboardFactoryImpl implements ViberKeyboardFactory {

	private static final int MIN_BUTTON_WIDTH = 1;

	@Override
	public MessageKeyboard createKeyboard(Collection<MessageKeyboardButton> keys) {
		MessageKeyboard messageKeyboard = new MessageKeyboard(new ArrayList<>(keys));
		log.info("Created keyboard: {}", messageKeyboard);
		return messageKeyboard;
	}

	/**
	 * Method allows to create simple keyboard
	 * @param buttons map, where key is button text and value is action body
	 * @return keyboard for KeyboardMessage
	 */
	@Override
	public MessageKeyboard createKeyboard(Map<String, String> buttons) {
		return createKeyboard(buttons, 1);
	}

	@Override
	public MessageKeyboard createKeyboard(Map<String, String> keys, int buttonsPerRow) {
		return new MessageKeyboard(createButtons(keys, buttonsPerRow));
	}

	private List<MessageKeyboardButton> createButtons(Map<String, String> keys, int buttonsPerRow) {
		return ofNullable(keys).orElse(Collections.emptyMap())
				.entrySet()
				.stream()
				.map(entry -> {
					MessageKeyboardButton button = new MessageKeyboardButton(entry.getKey(), entry.getValue());
					int width = MessageKeyboardButton.DEFAULT_WIDTH / buttonsPerRow;
					width = Math.max(width, MIN_BUTTON_WIDTH);
					button.setColumns(width);
					return button;
				})
				.collect(toList());
	}
}
