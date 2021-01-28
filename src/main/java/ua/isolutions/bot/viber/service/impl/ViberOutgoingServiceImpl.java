package ua.isolutions.bot.viber.service.impl;

import com.viber.bot.api.ViberBot;
import com.viber.bot.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.bot.service.MessageSender;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.factory.ViberResponseMessageConverterFactory;
import ua.isolutions.bot.commons.messages.message.response.Message;

@Service
@ConditionalOnBean(ViberConfig.class)
@RequiredArgsConstructor
@Log4j2
public class ViberOutgoingServiceImpl implements MessageSender {

	private final ViberResponseMessageConverterFactory viberBotMessageFactory;
	private final ViberBot bot;

	@Override
	public <T extends Message> boolean sendMessage(T message) {
		try {
			Message convert = viberBotMessageFactory.convert(message);
			bot.sendMessage(message.getUserId(), convert);
			return true;
		} catch (Exception e) {
			log.error("Cannot send message: " + message, e);
			return false;
		}
	}

	@Override
	public MessengerType getType() {
		return MessengerType.VIBER;
	}
}
