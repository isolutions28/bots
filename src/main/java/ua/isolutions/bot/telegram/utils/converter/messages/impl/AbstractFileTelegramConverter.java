package ua.isolutions.bot.telegram.utils.converter.messages.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ua.isolutions.bot.commons.file.service.FileReceiverService;
import ua.isolutions.bot.commons.messages.message.response.impl.FileMessage;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;

import static java.util.Objects.nonNull;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ConditionalOnBean(TelegramBotConfig.class)
public abstract class AbstractFileTelegramConverter extends AbstractKeyboardConverter {

	private FileReceiverService fileReceiverService;

	protected <T extends FileMessage> InputFile getFile(T message) {
		if (nonNull(message.getFileUrl())) {
			return new InputFile(fileReceiverService.downloadFile(message.getFileUrl()), message.getFileName());
		} else {
			return new InputFile(message.getFile(), message.getFileName());
		}
	}

	@Autowired
	public void setFileReceiverService(FileReceiverService fileReceiverService) {
		this.fileReceiverService = fileReceiverService;
	}
}
