package ua.isolutions.bot.commons.settings.service;


import ua.isolutions.bot.commons.settings.utils.SettingsConverter;

public interface SettingsService {

	String getSetting(String name);

	<T> T getSetting(String name, SettingsConverter<T> converter);
}
