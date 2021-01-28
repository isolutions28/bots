package ua.isolutions.bot.telegram.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import ua.isolutions.bot.commons.exceptions.NoSuchDataException;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.factory.TelegramResponseMessageConverterFactory;
import ua.isolutions.bot.telegram.utils.converter.messages.ResponseMessageConverter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
@ConditionalOnBean(TelegramBotConfig.class)
public class TelegramResponseMessageConverterFactoryImpl implements TelegramResponseMessageConverterFactory {
	@SuppressWarnings("rawtypes")
	private final Map<Class, ResponseMessageConverter> converters;

	@Autowired
	<T extends Serializable, T2 extends Message, T3 extends PartialBotApiMethod<T>>
	TelegramResponseMessageConverterFactoryImpl(Collection<ResponseMessageConverter<T2, T3>> converters) {
		this.converters = converters.stream()
				.collect(Collectors.toMap(ResponseMessageConverter::getConvertedClass, converter -> converter));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable, T2 extends Message, T3 extends PartialBotApiMethod<T>> T3 convert(T2 message) {
		ResponseMessageConverter<T2, T3> converter = (ResponseMessageConverter<T2, T3>) ofNullable(converters.get(message.getClass()))
				.orElseThrow(() -> new NoSuchDataException("Response converter not found: " + message.getClass()));
		return converter.convert(message);
	}
}
