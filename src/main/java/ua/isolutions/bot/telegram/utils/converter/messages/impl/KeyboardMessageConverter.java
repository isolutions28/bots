package ua.isolutions.bot.telegram.utils.converter.messages.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ua.isolutions.bot.commons.messages.message.response.impl.KeyboardMessage;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.utils.converter.messages.ResponseMessageConverter;

@Component
@ConditionalOnBean(TelegramBotConfig.class)
public class KeyboardMessageConverter extends AbstractKeyboardConverter implements ResponseMessageConverter<KeyboardMessage, SendMessage> {
	@Override
	public SendMessage convert(KeyboardMessage message) {
		SendMessage sendMessage = new SendMessage();

		sendMessage.setChatId(message.getUserId());
		sendMessage.setReplyMarkup(createKeyboard(message));
		sendMessage.setText("⌨");

		return sendMessage;
	}

	@Override
	public Class<KeyboardMessage> getConvertedClass() {
		return KeyboardMessage.class;
	}
}
