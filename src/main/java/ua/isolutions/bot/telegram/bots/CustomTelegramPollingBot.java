package ua.isolutions.bot.telegram.bots;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.factory.TelegramResponseMessageConverterFactory;
import ua.isolutions.bot.telegram.service.TelegramBotService;

@Component
@Log4j2
@RequiredArgsConstructor
@ConditionalOnBean(TelegramBotConfig.class)
@ConditionalOnExpression("T(org.springframework.util.StringUtils).isEmpty('${telegram.bot.webhook-url:}')")
public class CustomTelegramPollingBot extends TelegramLongPollingBot implements TelegramMessageSender {

    private final TelegramBotService service;
    private final TelegramBotConfig telegramBotConfig;
	private final TelegramResponseMessageConverterFactory telegramResponseMessageConverterFactory;

	@Override
	public void onUpdateReceived(Update update) {
		boolean b = service.processTelegramUpdate(update, this);
		log.info("Update processed with response: {}", b);
	}

    public String getBotUsername() {
        return telegramBotConfig.getName();
    }

    public String getBotToken() {
        return telegramBotConfig.getToken();
    }

	@Override
	public <T extends Message> boolean sendMessage(T message) {
		PartialBotApiMethod<org.telegram.telegrambots.meta.api.objects.Message> convert = telegramResponseMessageConverterFactory.convert(message);
		return sendInternalMessage(convert);
	}

	private boolean sendInternalMessage(PartialBotApiMethod<org.telegram.telegrambots.meta.api.objects.Message> message) {
		try {
			if (is(SendAnimation.class, message)) {
				execute((SendAnimation) message);
			} else if (is(SendAudio.class, message)) {
				execute((SendAudio) message);
			} else if (is(SendDocument.class, message)) {
				execute((SendDocument) message);
			} else if (is(SendPhoto.class, message)) {
				execute((SendPhoto) message);
			} else if (is(SendSticker.class, message)) {
				execute((SendSticker) message);
			} else if (is(SendVideo.class, message)) {
				execute((SendVideo) message);
			} else if (is(SendVideoNote.class, message)) {
				execute((SendVideoNote) message);
			} else if (is(SendVoice.class, message)) {
				execute((SendVoice) message);
			} else if (message instanceof BotApiMethod){
				execute((BotApiMethod<org.telegram.telegrambots.meta.api.objects.Message>)message);
			} else {
				throw new RuntimeException("Cannot find send method for message");
			}

			return true;
		} catch (Exception e) {
			log.error("Cannot send message. " + message, e);
			return false;
		}
	}

	private <T extends PartialBotApiMethod<?>> boolean is(Class<T> clazz, PartialBotApiMethod<org.telegram.telegrambots.meta.api.objects.Message> message) {
		return message.getClass() == clazz;
	}

}
