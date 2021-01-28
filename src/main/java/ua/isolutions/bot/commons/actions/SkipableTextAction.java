package ua.isolutions.bot.commons.actions;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class SkipableTextAction extends TextAction implements SkipableAction {

	@Override
	public MessageWrapper process(ActionMessage message) {
		if (skip(message)) {
			return processByNext(message);
		}

		return getDomainSendMessageWrapper(message);
	}
}
