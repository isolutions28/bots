package ua.isolutions.bot.commons.actions;

import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;

import java.util.Collection;

public abstract class TextAction extends AbstractAction {

    @Override
    public MessageWrapper process(ActionMessage message) {
		return getDomainSendMessageWrapper(message);
	}

	protected MessageWrapper getDomainSendMessageWrapper(ActionMessage message) {
		Collection<Message> sendMessage = sendMessage(text(textIdentifier(), message), keyboard(message));
		if (nextAction() != null) {
			return wrap(sendMessage, nextAction());
		}

		return wrap(sendMessage);
	}

	protected abstract String textIdentifier();

    protected KeyboardWrapper keyboard(ActionMessage message) {
        return null;
    }
}
