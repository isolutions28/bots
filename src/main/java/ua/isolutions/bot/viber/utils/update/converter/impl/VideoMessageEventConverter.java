package ua.isolutions.bot.viber.utils.update.converter.impl;

import com.viber.bot.message.Message;
import com.viber.bot.message.VideoMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.incoming.MessengerFileWrapper;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.update.converter.MessageEventConverter;
import ua.isolutions.bot.viber.utils.update.exception.WrongViberMessageTypeException;

import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.VIDEO;
import static ua.isolutions.bot.commons.messages.message.response.MessengerType.VIBER;

@Component
@ConditionalOnBean(ViberConfig.class)
public class VideoMessageEventConverter implements MessageEventConverter<VideoMessage> {
	private static final String EXPECTED_MESSAGE_TYPE = "video";

	@Override
	public ActionMessage convertCasted(VideoMessage message, String userId) {
		if (!EXPECTED_MESSAGE_TYPE.equalsIgnoreCase(message.getType())) {
			throw new WrongViberMessageTypeException(EXPECTED_MESSAGE_TYPE, message.getType(), "UserId: " + userId);
		}
		MessengerFileWrapper fileWrapper = new MessengerFileWrapper("", message.getUrl());
		return new ActionMessage(userId, fileWrapper, VIDEO, VIBER);
	}

	@Override
	public Class<VideoMessage> getConvertClass() {
		return VideoMessage.class;
	}

	@Override
	public VideoMessage cast(Message message) {
		return (VideoMessage) message;
	}
}
