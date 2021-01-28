package ua.isolutions.bot.telegram.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Log4j2
@ConditionalOnExpression("T(org.springframework.util.StringUtils).isEmpty('${telegram.bot.webhook-url:}')")
@ConditionalOnBean(TelegramBotConfig.class)
public class PoolingBotStartConfiguration {

	@Autowired
	public void initBot(@Value("${telegram.message.receiver:true}") boolean messageReceiver,
						LongPollingBot telegramBot,
						TelegramBotConfig config) {
		if (!messageReceiver) {
			return;
		}

		try {
			log.info("Starting polling telegram bot: " + config.getName());
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(telegramBot);
			log.info(config.getName() + " pooling bot started.");
		} catch (TelegramApiException e) {
			log.error("Can't start " + config.getName() + " pooling bot.", e);
			throw new RuntimeException(e);
		}
	}
}
