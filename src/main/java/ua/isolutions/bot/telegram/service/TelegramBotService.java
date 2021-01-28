package ua.isolutions.bot.telegram.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.isolutions.bot.telegram.bots.TelegramMessageSender;

public interface TelegramBotService {
    boolean processTelegramUpdate(Update update, TelegramMessageSender messageSender);
}
