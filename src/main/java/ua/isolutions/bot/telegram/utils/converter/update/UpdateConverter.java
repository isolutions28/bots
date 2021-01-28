package ua.isolutions.bot.telegram.utils.converter.update;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;

public interface UpdateConverter {

	ActionMessage convert(Update update);
}
