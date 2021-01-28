package ua.isolutions.bot.viber.utils.messages.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.file.dto.FileDto;
import ua.isolutions.bot.commons.messages.message.response.impl.FileMessage;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.messages.ViberResponseMessageConverter;

import static java.util.Objects.isNull;

@Component
@ConditionalOnBean(ViberConfig.class)
public class FileMessageViberConverter extends AbstractFileViberConverter implements ViberResponseMessageConverter<FileMessage, com.viber.bot.message.FileMessage> {

	@Override
	public com.viber.bot.message.FileMessage convert(FileMessage message) {
		com.viber.bot.message.FileMessage fileMessage = new com.viber.bot.message.FileMessage();
		FileDto fileDto = saveFile(message);
		fileMessage.setSizeInBytes(fileDto.getFileSize());
		fileMessage.setUrl(fileDto.getDownloadUrl());
		fileMessage.setFilename(isNull(message.getFileName()) ? "" : message.getFileName());
		fileMessage.setKeyboard(createKeyboard(message));
		return fileMessage;
	}

	@Override
	public Class<FileMessage> getConvertedClass() {
		return FileMessage.class;
	}
}
