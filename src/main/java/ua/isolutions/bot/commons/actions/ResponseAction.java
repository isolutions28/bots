package ua.isolutions.bot.commons.actions;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class ResponseAction extends AbstractAction {


	protected MessageWrapper createWrongDataResponse(ActionMessage message) {
        return wrap(sendMessage(text(wrongDataText(), message), wrongDataReplyKeyboard(message)), currentAction());
    }

    protected abstract String wrongDataText();

    protected KeyboardWrapper wrongDataReplyKeyboard(ActionMessage message) {
        return null;
    }
}
