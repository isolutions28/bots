package ua.isolutions.bot.viber.utils.update.converter.impl;

import com.viber.bot.message.Message;
import com.viber.bot.message.TextMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.update.converter.MessageEventConverter;
import ua.isolutions.bot.viber.utils.update.exception.WrongViberMessageTypeException;

import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.DICE;
import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.TEXT;
import static ua.isolutions.bot.commons.messages.message.response.MessengerType.VIBER;

@Component
@ConditionalOnBean(ViberConfig.class)
public class TextMessageEventConverter implements MessageEventConverter<TextMessage> {
	private static final String EXPECTED_MESSAGE_TYPE = "text";

	@Override
	public ActionMessage convertCasted(TextMessage message, String userId) {
		if (!EXPECTED_MESSAGE_TYPE.equalsIgnoreCase(message.getType())) {
			throw new WrongViberMessageTypeException(EXPECTED_MESSAGE_TYPE, message.getType(), "UserId: " + userId);
		}
		if ("(dice)".equals(message.getText()) || "\\uD83C\\uDFB2".equals(message.getText()) || "🎲".equals(message.getText())) {
			return new ActionMessage(userId, DICE, VIBER);
		}
		return new ActionMessage(userId, message.getText(), TEXT, VIBER);
	}

	@Override
	public Class<TextMessage> getConvertClass() {
		return TextMessage.class;
	}

	@Override
	public TextMessage cast(Message message) {
		return (TextMessage) message;
	}
}
