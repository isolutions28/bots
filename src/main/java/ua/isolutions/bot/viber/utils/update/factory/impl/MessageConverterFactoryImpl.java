package ua.isolutions.bot.viber.utils.update.factory.impl;

import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.settings.service.SettingsService;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.update.converter.MessageEventConverter;
import ua.isolutions.bot.viber.utils.update.exception.NoSuchMessageEventConverterException;
import ua.isolutions.bot.viber.utils.update.factory.MessageConverterFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.SUBSCRIBE;
import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.UNSUBSCRIBE;
import static ua.isolutions.bot.commons.messages.message.response.MessengerType.VIBER;

@Component
@ConditionalOnBean(ViberConfig.class)
public class MessageConverterFactoryImpl implements MessageConverterFactory {
	private static final String START_CONVERSATION_COMMAND = "start.conversation.command";
	private static final String LOGOUT_COMMAND = "logout.command";

	private final Map<Class<? extends Message>, MessageEventConverter<? extends Message>> converters = new HashMap<>();
	private final SettingsService settingsService;

	@Autowired
	public MessageConverterFactoryImpl(Collection<MessageEventConverter<? extends Message>> converters, SettingsService settingsService) {
		this.settingsService = settingsService;
		this.converters.putAll(converters.stream()
				.collect(Collectors.toMap(MessageEventConverter::getConvertClass, el -> el, (a, b) -> b)));
	}

	@Override
	public ActionMessage getMessage(Message message, String userId) {
		MessageEventConverter<? extends Message> converter = Optional.ofNullable(converters.get(message.getClass()))
				.orElseThrow(() -> new NoSuchMessageEventConverterException("No converter: " + message.getClass()));
		return converter.convert(message, userId);
	}

	@Override
	public ActionMessage getMessage(IncomingConversationStartedEvent event) {
		return new ActionMessage(event.getUser().getId(), settingsService.getSetting(START_CONVERSATION_COMMAND), SUBSCRIBE, VIBER);
	}

	@Override
	public ActionMessage getMessage(IncomingUnsubscribeEvent event) {
		return new ActionMessage(event.getUserId(), settingsService.getSetting(LOGOUT_COMMAND), UNSUBSCRIBE, VIBER);
	}
}
