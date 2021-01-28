package ua.isolutions.bot.viber.utils.messages.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.response.impl.TextMessage;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.messages.ViberResponseMessageConverter;

@Component
@ConditionalOnBean(ViberConfig.class)
public class TextMessageViberConverter extends AbstractKeyboardViberConverter implements ViberResponseMessageConverter<TextMessage, com.viber.bot.message.TextMessage> {

	@Override
	public com.viber.bot.message.TextMessage convert(TextMessage message) {
		return new com.viber.bot.message.TextMessage(message.getText(), createKeyboard(message));
	}

	@Override
	public Class<TextMessage> getConvertedClass() {
		return TextMessage.class;
	}
}
