package ua.isolutions.bot.commons.actions;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.exceptions.BotException;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;

import java.util.function.Function;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class SkipableButtonDataAction<T> extends DataAction<T> implements SkipableAction {

	private static final String DIVIDER = "__";

	@Override
	public MessageWrapper process(ActionMessage message) {
		if (skip(message)) {
			return this.processByNext(message);
		}

		return getDomainSendMessageWrapper(message);
	}

	@Override
	protected T getData(ActionMessage message) {
		try {
			return getConverter().apply(message.getMessage().split(DIVIDER)[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BotException("Can't split message. Wrong skip order? " + message.getMessage() + " " + getClass());
		}
	}

	protected abstract Function<String, T> getConverter();


}
