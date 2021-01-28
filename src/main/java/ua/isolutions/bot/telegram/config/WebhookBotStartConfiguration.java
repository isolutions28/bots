package ua.isolutions.bot.telegram.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;
import ua.isolutions.bot.telegram.bots.CustomTelegramWebhookBot;

@Configuration
@Log4j2
@ConditionalOnBean(WebhookBotConfig.class)
public class WebhookBotStartConfiguration {

	@Autowired
	public void initBot(@Value("${telegram.message.receiver:true}") boolean messageReceiver,
						@Value("${telegram.bot.name}") String botName,
						CustomTelegramWebhookBot telegramBot,
						WebhookBotConfig config) {
		if (!messageReceiver) {
			return;
		}

		try {
			log.info("Starting webhook telegram bot: " + botName + ", webhook: " + config.getWebhook());
			DefaultWebhook defaultWebhook = new DefaultWebhook();
			defaultWebhook.setInternalUrl("/telegram/" + config.getToken());
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
			botsApi.registerBot(telegramBot, new SetWebhook(config.getWebhook()));
			log.info(botName + " webhook bot started.");
		} catch (TelegramApiException e) {
			log.error("Can't start " + botName + " webhook bot.", e);
			throw new RuntimeException(e);
		}
	}
}
