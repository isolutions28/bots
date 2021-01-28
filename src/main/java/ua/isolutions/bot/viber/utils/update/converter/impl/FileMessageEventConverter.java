package ua.isolutions.bot.viber.utils.update.converter.impl;

import com.viber.bot.message.FileMessage;
import com.viber.bot.message.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.incoming.MessengerFileWrapper;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.update.converter.MessageEventConverter;
import ua.isolutions.bot.viber.utils.update.exception.WrongViberMessageTypeException;

import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.FILE;
import static ua.isolutions.bot.commons.messages.message.response.MessengerType.VIBER;

@Component
@ConditionalOnBean(ViberConfig.class)
public class FileMessageEventConverter implements MessageEventConverter<FileMessage> {
	private static final String EXPECTED_MESSAGE_TYPE = "file";

	@Override
	public ActionMessage convertCasted(FileMessage message, String userId) {
		if (!EXPECTED_MESSAGE_TYPE.equalsIgnoreCase(message.getType())) {
			throw new WrongViberMessageTypeException(EXPECTED_MESSAGE_TYPE, message.getType(), "UserId: " + userId);
		}
		MessengerFileWrapper file = new MessengerFileWrapper("", message.getUrl());
		return new ActionMessage(userId, file, FILE, VIBER);
	}

	@Override
	public Class<FileMessage> getConvertClass() {
		return FileMessage.class;
	}

	@Override
	public FileMessage cast(Message message) {
		return (FileMessage) message;
	}
}
