package ua.isolutions.bot.viber.utils.messages.impl;

import com.viber.bot.message.MessageKeyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.factory.ViberKeyboardFactory;

import static java.util.Objects.isNull;

@Component
@ConditionalOnBean(ViberConfig.class)
public abstract class AbstractKeyboardViberConverter {
	private ViberKeyboardFactory viberKeyboardFactory;

	protected MessageKeyboard createKeyboard(Message message) {
		if (isNull(message.getKeyboard())) {
			return null;
		}
		return viberKeyboardFactory.createKeyboard(message.getKeyboard().getButtonsAsMap(), message.getKeyboard().getButtonsPerRow());
	}

	@Autowired
	public void setViberKeyboardFactory(ViberKeyboardFactory viberKeyboardFactory) {
		this.viberKeyboardFactory = viberKeyboardFactory;
	}
}
