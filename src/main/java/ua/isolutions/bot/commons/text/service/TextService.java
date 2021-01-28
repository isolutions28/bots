package ua.isolutions.bot.commons.text.service;


import ua.isolutions.bot.commons.dto.ChatIdMessenger;
import ua.isolutions.bot.commons.text.dao.Locale;

public interface TextService {

	String getText(String text, ChatIdMessenger dto);
	String getText(String text, Locale locale);
	String getText(String text);
}
