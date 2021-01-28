package ua.isolutions.bot.commons.settings.utils;

public interface SettingsConverter<T> {

	T convert(String setting);
}
