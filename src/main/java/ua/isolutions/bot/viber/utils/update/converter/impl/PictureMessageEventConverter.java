package ua.isolutions.bot.viber.utils.update.converter.impl;

import com.viber.bot.message.Message;
import com.viber.bot.message.PictureMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.incoming.MessengerFileWrapper;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.update.converter.MessageEventConverter;
import ua.isolutions.bot.viber.utils.update.exception.WrongViberMessageTypeException;

import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.PICTURE;
import static ua.isolutions.bot.commons.messages.message.response.MessengerType.VIBER;

@Component
@ConditionalOnBean(ViberConfig.class)
public class PictureMessageEventConverter implements MessageEventConverter<PictureMessage> {
	private static final String EXPECTED_MESSAGE_TYPE = "picture";

	@Override
	public ActionMessage convertCasted(PictureMessage message, String userId) {
		if (!EXPECTED_MESSAGE_TYPE.equalsIgnoreCase(message.getType())) {
			throw new WrongViberMessageTypeException(EXPECTED_MESSAGE_TYPE, message.getType(), "UserId: " + userId);
		}
		MessengerFileWrapper fileWrapper = new MessengerFileWrapper(message.getText(), message.getUrl());
		return new ActionMessage(userId, fileWrapper, PICTURE, VIBER);
	}

	@Override
	public Class<PictureMessage> getConvertClass() {
		return PictureMessage.class;
	}

	@Override
	public PictureMessage cast(Message message) {
		return (PictureMessage) message;
	}
}
