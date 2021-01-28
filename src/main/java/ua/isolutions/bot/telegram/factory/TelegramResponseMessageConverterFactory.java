package ua.isolutions.bot.telegram.factory;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import ua.isolutions.bot.commons.messages.message.response.Message;

import java.io.Serializable;

public interface TelegramResponseMessageConverterFactory {

	<T extends Serializable, T2 extends Message, T3 extends PartialBotApiMethod<T>> T3 convert(T2 message);
}
