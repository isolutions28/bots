package ua.isolutions.bot.telegram.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.isolutions.bot.commons.bot.service.BotIncomingService;
import ua.isolutions.bot.commons.config.Log4jMDCFilter;
import ua.isolutions.bot.commons.config.Log4jUserUuidMDCFilter;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.commons.sting_generator.StringGeneratorService;
import ua.isolutions.bot.telegram.bots.TelegramMessageSender;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.service.TelegramBotService;
import ua.isolutions.bot.telegram.utils.converter.update.UpdateConverter;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

@Service
@Log4j2
@RequiredArgsConstructor
@ConditionalOnBean(TelegramBotConfig.class)
public class TelegramBotServiceImpl implements TelegramBotService {

	private final BotIncomingService botIncomingService;
    private final ExecutorService executorService;
    private final UpdateConverter updateConverter;
    private final StringGeneratorService stringGeneratorService;

	@Override
    public boolean processTelegramUpdate(Update update, TelegramMessageSender messageSender) {
        try {
            executorService.submit(() -> {
                try {
                    ActionMessage actionMessage = updateConverter.convert(update);
					addLog4j2Data(actionMessage.getChatIdMessenger().getChatId());
					log.info("Message received. {}", actionMessage);
                    Collection<Message> message = botIncomingService.processMessage(actionMessage).get();
					sendMessages(messageSender, message, actionMessage.getChatIdMessenger().getChatId());
					return true;
                } catch (Exception e) {
                    log.error("Error while processing update", e);
                    return false;
                } finally {
                	MDC.clear();
				}
            });
        } catch (Exception e) {
            log.error("Error while submitting update", e);
            return false;
        }
        return true;
    }

	private void addLog4j2Data(String chatId) {
		MDC.put(Log4jUserUuidMDCFilter.MDC_USER_ID_TOKEN_KEY, chatId);
		MDC.put(Log4jMDCFilter.MDC_UUID_TOKEN_KEY, stringGeneratorService.uuidString(1));
	}

	private void sendMessages(TelegramMessageSender messageSender, Collection<Message> messages, String userId) {
    	messages.forEach(message -> sendMessage(messageSender, message, userId));
	}

	private void sendMessage(TelegramMessageSender messageSender, Message message, String userId) {
		message.setUserId(userId);
		log.info("Sending response to user: {}", message);
		boolean b = messageSender.sendMessage(message);
		log.info("Message send: {}", b);
	}
}
