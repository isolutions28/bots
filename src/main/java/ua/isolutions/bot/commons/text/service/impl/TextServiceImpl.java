package ua.isolutions.bot.commons.text.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.cache.CacheService;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;
import ua.isolutions.bot.commons.text.dao.Locale;
import ua.isolutions.bot.commons.text.dao.Text;
import ua.isolutions.bot.commons.text.domain.TextRepository;
import ua.isolutions.bot.commons.text.exception.NoSuchTextException;
import ua.isolutions.bot.commons.text.service.LocaleService;
import ua.isolutions.bot.commons.text.service.TextService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {
	private final TextRepository textRepository;
	private final CacheService cache;
	private final LocaleService localeService;

	@Override
	public String getText(String text, ChatIdMessenger dto) {
		Text textEntity = getTextEntity(text);
		Locale userLocale = localeService.getUserLocale(dto);
		return formatLocalizedText(userLocale, textEntity);
	}

	private void saveToCache(String text, Text textEntity) {
		cache.addValueExpired(text, textEntity);
	}

	@Override
	public String getText(String text, Locale locale) {
		Text textEntity = getTextFromDataBase(text);
		return formatLocalizedText(locale, textEntity);
	}

	private Text getTextEntity(String text) {
		return getFromCache(text)
				.orElseGet(() -> getTextFromDataBase(text));
	}

	@Override
	public String getText(String text) {
		return getText(text, Locale.DEFAULT_LOCALE);
	}

	private String formatLocalizedText(Locale locale, Text textEntity) {
		return textEntity.getByLocale(locale);
	}

	private Optional<Text> getFromCache(String text) {
		return cache.getSilent(text, Text.class);
	}

	private Text getTextFromDataBase(String text) {
		Text textFromDB = textRepository.findOneByName(text)
				.orElseThrow(() -> new NoSuchTextException("Text with name: " + text + " not found."));
		saveToCache(textFromDB.getName(), textFromDB);
		return textFromDB;
	}
}
