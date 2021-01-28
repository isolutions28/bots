package ua.isolutions.bot.telegram.utils.converter.messages;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import ua.isolutions.bot.commons.messages.message.response.Message;

import java.io.Serializable;

public interface ResponseMessageConverter<T extends Message, T2 extends PartialBotApiMethod<? extends Serializable>> {
	T2 convert(T message);

	Class<T> getConvertedClass();
}
