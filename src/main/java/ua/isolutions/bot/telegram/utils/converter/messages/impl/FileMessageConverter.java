package ua.isolutions.bot.telegram.utils.converter.messages.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ua.isolutions.bot.commons.messages.message.response.impl.FileMessage;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.utils.converter.messages.ResponseMessageConverter;

@Component
@ConditionalOnBean(TelegramBotConfig.class)
public class FileMessageConverter extends AbstractFileTelegramConverter implements ResponseMessageConverter<FileMessage, SendDocument> {

	@Override
	public SendDocument convert(FileMessage message) {
		SendDocument sendDocument = new SendDocument();

		sendDocument.setChatId(message.getUserId());

		InputFile inputFile = new InputFile(message.getFile(), message.getFileName());
		sendDocument.setDocument(getFile(message));

		sendDocument.setReplyMarkup(createKeyboard(message));
		return sendDocument;
	}

	@Override
	public Class<FileMessage> getConvertedClass() {
		return FileMessage.class;
	}
}
