package ua.isolutions.bot.viber.utils.messages.impl;

import com.viber.bot.message.TextMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.response.impl.KeyboardMessage;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.messages.ViberResponseMessageConverter;

@Component
@ConditionalOnBean(ViberConfig.class)
public class KeyboardMessageViberConverter extends AbstractKeyboardViberConverter implements ViberResponseMessageConverter<KeyboardMessage, TextMessage> {

	@Override
	public TextMessage convert(KeyboardMessage message) {
		return new TextMessage("⌨", createKeyboard(message));
	}

	@Override
	public Class<KeyboardMessage> getConvertedClass() {
		return KeyboardMessage.class;
	}
}
