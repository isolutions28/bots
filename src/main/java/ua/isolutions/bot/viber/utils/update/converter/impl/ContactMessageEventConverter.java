package ua.isolutions.bot.viber.utils.update.converter.impl;


import com.viber.bot.message.ContactMessage;
import com.viber.bot.message.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.update.converter.MessageEventConverter;
import ua.isolutions.bot.viber.utils.update.exception.WrongViberMessageTypeException;

import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.CONTACT;
import static ua.isolutions.bot.commons.messages.message.response.MessengerType.VIBER;

@Component
@ConditionalOnBean(ViberConfig.class)
public class ContactMessageEventConverter implements MessageEventConverter<ContactMessage> {
	private static final String EXPECTED_MESSAGE_TYPE = "contact";

	@Override
	public ActionMessage convertCasted(ContactMessage message, String userId) {
		if (!EXPECTED_MESSAGE_TYPE.equalsIgnoreCase(message.getType())) {
			throw new WrongViberMessageTypeException(EXPECTED_MESSAGE_TYPE, message.getType(), "UserId: " + userId);
		}
		return new ActionMessage(userId, message.getContact().getPhoneNumber(), CONTACT, VIBER);
	}

	@Override
	public Class<ContactMessage> getConvertClass() {
		return ContactMessage.class;
	}

	@Override
	public ContactMessage cast(Message message) {
		return (ContactMessage) message;
	}
}
