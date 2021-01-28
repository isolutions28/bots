package ua.isolutions.bot.viber.config;

import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.ViberBot;
import com.viber.bot.profile.BotProfile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.isolutions.bot.viber.retranslator.utils.bot.ViberReTranslatorBot;
import ua.isolutions.bot.viber.retranslator.utils.client.RetranslatorClient;
import ua.innovations.commons.http.BaseSerializer;

import java.util.concurrent.ExecutorService;

@Configuration
@ConditionalOnBean(ViberConfig.class)
public class ViberBotConfig {

	@Bean
	@ConditionalOnProperty(name = "enable.viber.retranslator", matchIfMissing = true)
	ViberBot viberBot(ViberConfig viberConfig,
					  BaseSerializer baseSerializer,
					  ExecutorService incomingProcessor,
					  ExecutorService outgoingProcessor) {
		return new ViberBot(new BotProfile(viberConfig.getName(), viberConfig.getAvatar()), viberConfig.getAuthToken(), baseSerializer, incomingProcessor, outgoingProcessor);
	}

	@Bean
	@ConditionalOnProperty(name = "enable.viber.retranslator", havingValue = "true")
	ViberBot devViberBot(ViberConfig viberConfig,
						 BaseSerializer baseSerializer,
						 RetranslatorClient retranslatorClient,
						 ExecutorService incomingProcessor,
						 ExecutorService outgoingProcessor) {
		return new ViberReTranslatorBot(new BotProfile(viberConfig.getName()), viberConfig.getAuthToken(), baseSerializer, retranslatorClient, incomingProcessor, outgoingProcessor);
	}

	@Bean
	ViberSignatureValidator signatureValidator(ViberConfig viberConfig) {
		return new ViberSignatureValidator(viberConfig.getAuthToken());
	}
}
