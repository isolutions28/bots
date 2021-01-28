package ua.isolutions.bot.viber.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.exceptions.NoSuchDataException;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.factory.ViberResponseMessageConverterFactory;
import ua.isolutions.bot.viber.utils.messages.ViberResponseMessageConverter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
@ConditionalOnBean(ViberConfig.class)
public class ViberResponseMessageConverterFactoryImpl implements ViberResponseMessageConverterFactory {
	@SuppressWarnings("rawtypes")
	private final Map<Class, ViberResponseMessageConverter> converters;

	@Autowired
	<T extends Message, T2 extends com.viber.bot.message.Message>
	ViberResponseMessageConverterFactoryImpl(Collection<ViberResponseMessageConverter<T, T2>> converters) {
		this.converters = converters.stream()
				.collect(Collectors.toMap(ViberResponseMessageConverter::getConvertedClass, converter -> converter));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Message, T2 extends com.viber.bot.message.Message> T2 convert(T message) {
		ViberResponseMessageConverter<T, T2> converter = (ViberResponseMessageConverter<T, T2>) ofNullable(converters.get(message.getClass()))
				.orElseThrow(() -> new NoSuchDataException("Viber response converter not found: " + message.getClass()));
		return converter.convert(message);
	}
}
