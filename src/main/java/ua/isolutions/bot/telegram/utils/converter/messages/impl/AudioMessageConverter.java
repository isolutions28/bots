package ua.isolutions.bot.telegram.utils.converter.messages.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import ua.isolutions.bot.commons.messages.message.response.impl.AudioMessage;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.utils.converter.messages.ResponseMessageConverter;

@Component
@ConditionalOnBean(TelegramBotConfig.class)
public class AudioMessageConverter extends AbstractFileTelegramConverter implements ResponseMessageConverter<AudioMessage, SendAudio> {

	@Override
	public SendAudio convert(AudioMessage message) {
		SendAudio sendAudio = new SendAudio();
		sendAudio.setChatId(message.getUserId());
		sendAudio.setAudio(getFile(message));
		sendAudio.setReplyMarkup(createKeyboard(message));
		return sendAudio;
	}

	@Override
	public Class<AudioMessage> getConvertedClass() {
		return AudioMessage.class;
	}
}
