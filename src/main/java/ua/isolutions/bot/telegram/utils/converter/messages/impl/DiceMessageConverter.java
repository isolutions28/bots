package ua.isolutions.bot.telegram.utils.converter.messages.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ua.isolutions.bot.commons.messages.message.response.impl.DiceMessage;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.utils.converter.messages.ResponseMessageConverter;

@Component
@ConditionalOnBean(TelegramBotConfig.class)
public class DiceMessageConverter extends AbstractKeyboardConverter implements ResponseMessageConverter<DiceMessage, SendMessage> {

	@Override
	public SendMessage convert(DiceMessage message) {
		SendMessage sendMessage = new SendMessage();

		sendMessage.setReplyMarkup(createKeyboard(message));

		sendMessage.setChatId(message.getUserId());

		sendMessage.setText(message.getActualMessage());

		return sendMessage;
	}

	@Override
	public Class<DiceMessage> getConvertedClass() {
		return DiceMessage.class;
	}
}
