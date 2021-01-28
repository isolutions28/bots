package ua.isolutions.bot.telegram.utils.converter.messages.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ua.isolutions.bot.commons.messages.message.response.impl.ImageMessage;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.utils.converter.messages.ResponseMessageConverter;

@Component
@ConditionalOnBean(TelegramBotConfig.class)
public class ImageMessageConverter extends AbstractFileTelegramConverter implements ResponseMessageConverter<ImageMessage, SendPhoto> {

	@Override
	public SendPhoto convert(ImageMessage message) {
		SendPhoto sendPhoto = new SendPhoto();

		sendPhoto.setChatId(message.getUserId());

		InputFile inputFile = new InputFile(message.getFile(), message.getFileName());
		sendPhoto.setPhoto(getFile(message));

		sendPhoto.setReplyMarkup(createKeyboard(message));

		return sendPhoto;
	}

	@Override
	public Class<ImageMessage> getConvertedClass() {
		return ImageMessage.class;
	}
}
