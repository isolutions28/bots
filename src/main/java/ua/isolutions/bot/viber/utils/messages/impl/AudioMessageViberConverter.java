package ua.isolutions.bot.viber.utils.messages.impl;

import com.viber.bot.message.FileMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.file.dto.FileDto;
import ua.isolutions.bot.commons.messages.message.response.impl.AudioMessage;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.messages.ViberResponseMessageConverter;

import static java.util.Objects.isNull;

@Component
@ConditionalOnBean(ViberConfig.class)
public class AudioMessageViberConverter extends AbstractFileViberConverter implements ViberResponseMessageConverter<AudioMessage, FileMessage> {

	@Override
	public FileMessage convert(AudioMessage message) {
		FileMessage fileMessage = new FileMessage();
		FileDto fileDto = saveFile(message);
		fileMessage.setUrl(fileDto.getDownloadUrl());
		fileMessage.setSizeInBytes(fileDto.getFileSize());
		fileMessage.setFilename(isNull(message.getFileName()) ? "" : message.getFileName());
		fileMessage.setKeyboard(createKeyboard(message));
		return fileMessage;
	}

	@Override
	public Class<AudioMessage> getConvertedClass() {
		return AudioMessage.class;
	}
}
