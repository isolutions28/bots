package ua.isolutions.bot.commons.actions;

import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;

public interface Action {
    MessageWrapper processMessage(ActionMessage message);
    Action getNext();
    void setNext(Action action);
    void setPrevious(Action action);
}
