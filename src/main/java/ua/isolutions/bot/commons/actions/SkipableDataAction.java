package ua.isolutions.bot.commons.actions;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Log4j2
public abstract class SkipableDataAction<T> extends DataAction<T> implements SkipableAction {

	@Override
	public MessageWrapper process(ActionMessage message) {
		if (skip(message)) {
			return processByNext(message);
		}

		return getDomainSendMessageWrapper(message);
	}
}
