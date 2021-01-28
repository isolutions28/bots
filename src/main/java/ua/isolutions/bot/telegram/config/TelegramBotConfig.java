package ua.isolutions.bot.telegram.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "telegram.bot.token")
@Getter
public class TelegramBotConfig {

	@Value("${telegram.bot.name}")
    private String name;
	@Value("${telegram.bot.token}")
    private String token;
}

