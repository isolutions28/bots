package ua.isolutions.bot.telegram.utils.converter.messages.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ua.isolutions.bot.commons.messages.message.response.impl.TextMessage;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.utils.converter.messages.ResponseMessageConverter;

@Component
@ConditionalOnBean(TelegramBotConfig.class)
public class TextMessageConverter extends AbstractKeyboardConverter implements ResponseMessageConverter<TextMessage, SendMessage> {

	@Override
	public SendMessage convert(TextMessage message) {
		SendMessage sendMessage = new SendMessage(message.getUserId(), message.getText());

		sendMessage.setReplyMarkup(createKeyboard(message));

		return sendMessage;
	}

	@Override
	public Class<TextMessage> getConvertedClass() {
		return TextMessage.class;
	}
}
