package ua.isolutions.bot.telegram.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = {"telegram.bot.webhook-url", "telegram.bot.token"})
@Getter
public class WebhookBotConfig extends TelegramBotConfig {

	@Value("${telegram.bot.token}")
	private String path;
	@Value("${telegram.bot.webhook-url:#{null}}")
	private String webhook;
}
