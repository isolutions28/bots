package ua.isolutions.bot.commons.messages.keyboard.factory.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardButtonWrapper;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;
import ua.isolutions.bot.commons.messages.keyboard.factory.KeyboardFactory;

import java.util.*;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Component
@Log4j2
public class KeyboardFactoryImpl implements KeyboardFactory {
	@Override
	public KeyboardWrapper createKeyboard(Collection<KeyboardButtonWrapper> keys) {
		return createKeyboard(keys, 1);
	}

	@Override
	public KeyboardWrapper createKeyboard(Collection<KeyboardButtonWrapper> keys, int buttonsPerRow) {
		KeyboardWrapper messageKeyboard = new KeyboardWrapper(new ArrayList<>(keys), buttonsPerRow);
		log.info("Created abstract keyboard: {}", messageKeyboard);
		return messageKeyboard;
	}

	@Override
	public KeyboardWrapper createKeyboard(Map<String, String> keys) {
		return createKeyboard(keys, 1);
	}

	@Override
	public KeyboardWrapper createKeyboard(Map<String, String> keys, int buttonsPerRow) {
		KeyboardWrapper keyboardWrapper = new KeyboardWrapper(createButtons(keys), buttonsPerRow);
		log.info("Created abstract keyboard: {}", keyboardWrapper);
		return keyboardWrapper;
	}

	private List<KeyboardButtonWrapper> createButtons(Map<String, String> keys) {
		return ofNullable(keys).orElse(Collections.emptyMap())
				.entrySet()
				.stream()
				.map(entry -> new KeyboardButtonWrapper(entry.getKey(), entry.getValue()))
				.collect(toList());
	}
}

