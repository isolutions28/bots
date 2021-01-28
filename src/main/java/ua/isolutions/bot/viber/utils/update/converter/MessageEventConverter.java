package ua.isolutions.bot.viber.utils.update.converter;

import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import com.viber.bot.message.Message;

public interface MessageEventConverter<T extends Message> {

	default ActionMessage convert(Message message, String userId) {
		return convertCasted(cast(message), userId);
	}

	ActionMessage convertCasted(T message, String userId);

	Class<T> getConvertClass();

	T cast(Message message);
}
