package ua.isolutions.bot.viber.utils.messages;

import com.viber.bot.message.Message;
import ua.isolutions.bot.commons.messages.message.response.Message;

public interface ViberResponseMessageConverter<T extends Message, T2 extends Message> {

	T2 convert(T message);

	Class<T> getConvertedClass();
}
