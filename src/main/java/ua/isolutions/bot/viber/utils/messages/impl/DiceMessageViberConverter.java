package ua.isolutions.bot.viber.utils.messages.impl;

import com.viber.bot.message.TextMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.response.impl.DiceMessage;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.messages.ViberResponseMessageConverter;

@Component
@ConditionalOnBean(ViberConfig.class)
public class DiceMessageViberConverter extends AbstractKeyboardViberConverter implements ViberResponseMessageConverter<DiceMessage, TextMessage> {

	@Override
	public TextMessage convert(DiceMessage message) {
		return new TextMessage(message.getActualMessage(), createKeyboard(message));
	}

	@Override
	public Class<DiceMessage> getConvertedClass() {
		return DiceMessage.class;
	}
}
