package ua.isolutions.bot.viber.utils.update.factory;

import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;

public interface MessageConverterFactory {
	ActionMessage getMessage(Message message, String userId);

	ActionMessage getMessage(IncomingConversationStartedEvent event);

	ActionMessage getMessage(IncomingUnsubscribeEvent event);
}
