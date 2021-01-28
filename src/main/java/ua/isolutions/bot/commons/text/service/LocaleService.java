package ua.isolutions.bot.commons.text.service;


import ua.isolutions.bot.commons.text.dao.Locale;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;

public interface LocaleService {
	Locale getUserLocale(ChatIdMessenger chatDto);

	Locale setUserLocale(ChatIdMessenger chatDto, String locale);
}
