package ua.isolutions.bot.commons.bot.factory.impl;

import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.bot.factory.MessageSenderFactory;
import ua.isolutions.bot.commons.bot.service.MessageSender;
import ua.isolutions.bot.commons.exceptions.NoSuchDataException;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MessageSenderFactoryImpl implements MessageSenderFactory {

	private final Map<MessengerType, MessageSender> senderMap = new HashMap<>();

	public MessageSenderFactoryImpl(List<MessageSender> senderList) {
		senderList.forEach(sender -> senderMap.put(sender.getType(), sender));
	}

	@Override
	public MessageSender getSender(MessengerType messengerType) {
		return Optional.ofNullable(senderMap.get(messengerType))
				.orElseThrow(() -> new NoSuchDataException("No message sender found for type: " + messengerType));
	}
}
