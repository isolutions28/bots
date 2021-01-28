package ua.isolutions.bot.viber.service.impl;

import com.viber.bot.api.ViberBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.bot.service.BotIncomingService;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.factory.ViberResponseMessageConverterFactory;
import ua.isolutions.bot.viber.utils.update.factory.MessageConverterFactory;

import java.util.ArrayList;
import java.util.Collection;

@Service
@ConditionalOnBean(ViberConfig.class)
@RequiredArgsConstructor
@Log4j2
public class ViberBotIncomingServiceImpl implements ApplicationListener<ApplicationReadyEvent> {

	private final ViberBot bot;
	private final MessageConverterFactory messageConverter;
	private final ViberResponseMessageConverterFactory responseConverter;
	private final BotIncomingService botIncomingService;

	@Value("${viber.bot.webhook-url}")
	private String webhookUrl;
	@Value("${viber.message.receiver:true}")
	private boolean messageSender;

	@Override
	public void onApplicationEvent(@NonNull ApplicationReadyEvent appReadyEvent) {
		if (messageSender) {
			try {
				log.info("Starting viber bot: " + bot.getBotProfile().getName());
				int status = bot.setWebhook(webhookUrl).getStatus();
				log.info("Viber webhook result: " + status);
			} catch (Exception e) {
				log.warn("Cannot webhook viber", e);
			}
		}

		bot.onConversationStarted(event -> {
			log.info("Conversation started {}", event.getUser().getId());
			try {
				Message next = botIncomingService.processMessage(messageConverter.getMessage(event))
						.get()
						.iterator()
						.next();// onConversationStarted can process only 1 text message
				return responseConverter.convert(next);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		});

		bot.onMessageReceived((event, viberMessage, response) -> {
			try {
				Collection<Message> messages = botIncomingService.processMessage(messageConverter.getMessage(event.getMessage(), event.getSender().getId())).get();
				ArrayList<com.viber.bot.message.Message> responseMessages = new ArrayList<>(messages.size());
				for (Message message: messages) { // cannot use stream cause of dynamic types
					responseMessages.add(responseConverter.convert(message));
				}
				response.send(responseMessages);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		bot.onUnsubscribe((event) -> {
			log.info("User unsubscribed {}", event.getUserId());
			ActionMessage message = messageConverter.getMessage(event);
			botIncomingService.processMessage(message);
		});
	}
}
