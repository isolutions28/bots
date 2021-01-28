package ua.isolutions.bot.viber.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "viber.bot.token")
@Getter
public class ViberConfig {

	@Value("${viber.bot.token}")
	private String authToken;
	@Value("${viber.bot.name}")
	private String name;
	@Value("${viber.bot.avatar:@null}")
	private String avatar;
}
