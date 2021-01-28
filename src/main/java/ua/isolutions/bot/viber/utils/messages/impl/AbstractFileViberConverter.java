package ua.isolutions.bot.viber.utils.messages.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.file.dto.FileDto;
import ua.isolutions.bot.commons.file.service.FileReceiverService;
import ua.isolutions.bot.commons.messages.message.response.impl.FileMessage;
import ua.isolutions.bot.viber.config.ViberConfig;

import static java.util.Objects.nonNull;

@Component
@ConditionalOnBean(ViberConfig.class)
public abstract class AbstractFileViberConverter extends AbstractKeyboardViberConverter {

	private FileReceiverService fileReceiverService;

	protected <T extends FileMessage> FileDto saveFile(FileMessage message) {
		if (nonNull(message.getFileUrl())) {
			return new FileDto(message.getFileUrl(), message.getFileSize());
		} else {
			return fileReceiverService.uploadFile(message.getFileName(), message.getFile());
		}
	}

	@Autowired
	public void setFileReceiverService(FileReceiverService fileReceiverService) {
		this.fileReceiverService = fileReceiverService;
	}
}
