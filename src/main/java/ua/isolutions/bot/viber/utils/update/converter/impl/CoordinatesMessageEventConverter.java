package ua.isolutions.bot.viber.utils.update.converter.impl;

import com.viber.bot.message.LocationMessage;
import com.viber.bot.message.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.incoming.LocationWrapper;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.update.converter.MessageEventConverter;
import ua.isolutions.bot.viber.utils.update.exception.WrongViberMessageTypeException;

import static ua.isolutions.bot.commons.messages.message.response.MessengerType.VIBER;

@Component
@ConditionalOnBean(ViberConfig.class)
public class CoordinatesMessageEventConverter implements MessageEventConverter<LocationMessage> {
	private static final String EXPECTED_MESSAGE_TYPE = "location";

	@Override
	public ActionMessage convertCasted(LocationMessage message, String userId) {
		if (!EXPECTED_MESSAGE_TYPE.equalsIgnoreCase(message.getType())) {
			throw new WrongViberMessageTypeException(EXPECTED_MESSAGE_TYPE, message.getType(), "UserId: " + userId);
		}
		return new ActionMessage(userId, new LocationWrapper(message.getLocation().getLongitude(), message.getLocation().getLatitude()), VIBER);
	}

	@Override
	public Class<LocationMessage> getConvertClass() {
		return LocationMessage.class;
	}

	@Override
	public LocationMessage cast(Message message) {
		return (LocationMessage) message;
	}
}
