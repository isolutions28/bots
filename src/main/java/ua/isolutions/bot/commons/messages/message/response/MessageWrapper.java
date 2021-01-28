package ua.isolutions.bot.commons.messages.message.response;

import lombok.Data;
import ua.isolutions.bot.commons.actions.Action;

import java.util.Collection;
import java.util.Collections;

@Data
public class MessageWrapper {

    private final Collection<? extends Message> message;
    private final Action lastAction;
    private final boolean unregister;
    private boolean showAllCommands;

	public <T extends Message>  MessageWrapper(T message, Action lastAction, boolean unregister) {
		this(Collections.singletonList(message), lastAction, unregister);
	}

    public <T extends Message> MessageWrapper(Collection<T> message, Action lastAction, boolean unregister) {
        this(message, lastAction, unregister, true);
    }

	public <T extends Message> MessageWrapper(Collection<T> message, Action lastAction, boolean unregister, boolean showAllCommands) {
		this.message = message;
		this.lastAction = lastAction;
		this.unregister = unregister;
		this.showAllCommands = showAllCommands;
	}

	public <T extends Message> MessageWrapper(T message, Action lastAction, boolean unregister, boolean showAllCommands) {
		this(Collections.singletonList(message), lastAction, unregister, showAllCommands);
	}
}
