package ua.isolutions.bot.commons.bot.service;

import org.springframework.scheduling.annotation.Async;
import ua.isolutions.bot.commons.messages.message.response.Message;

import java.util.concurrent.Future;

public interface BotOutgoingService {
	@Async
	<T extends Message> Future<Boolean> sendMessage(T message);
}
