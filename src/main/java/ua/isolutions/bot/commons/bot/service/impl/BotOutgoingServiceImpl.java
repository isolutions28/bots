package ua.isolutions.bot.commons.bot.service.impl;

import com.google.common.util.concurrent.Futures;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.bot.factory.MessageSenderFactory;
import ua.isolutions.bot.commons.bot.service.BotOutgoingService;
import ua.isolutions.bot.commons.bot.service.MessageSender;
import ua.isolutions.bot.commons.messages.message.response.Message;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Log4j2
public class BotOutgoingServiceImpl implements BotOutgoingService {

	private final MessageSenderFactory messageSenderFactory;

	@Async
	@Override
	public <T extends Message> Future<Boolean> sendMessage(T message) {
		try {
			MessageSender sender = messageSenderFactory.getSender(message.getMessengerType());
			return Futures.immediateFuture(sender.sendMessage(message));
		} catch (Exception e) {
			log.error("Cannot send message " + message, e);
			return Futures.immediateFuture(false);
		}
	}
}
