package ua.isolutions.bot.viber.factory;

import ua.isolutions.bot.commons.messages.message.response.Message;

public interface ViberResponseMessageConverterFactory {
	<T extends Message, T2 extends com.viber.bot.message.Message> T2 convert(T message);

}
