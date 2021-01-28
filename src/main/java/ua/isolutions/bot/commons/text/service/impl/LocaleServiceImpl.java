package ua.isolutions.bot.commons.text.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.cache.CacheService;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;
import ua.isolutions.bot.commons.text.dao.Locale;
import ua.isolutions.bot.commons.text.dao.UserLocale;
import ua.isolutions.bot.commons.text.domain.UserLocaleRepository;
import ua.isolutions.bot.commons.text.service.LocaleService;

@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

	private static final String USER_LOCALE_CACHE_KEY = "user.locale.%s.%s";

	private final UserLocaleRepository userLocaleRepository;
	private final CacheService cacheService;

	@Override
	public Locale getUserLocale(ChatIdMessenger chatDto) {
		return cacheService.getSilent(localeCacheKey(chatDto), Locale.class)
				.orElseGet(() -> {
					Locale userLocale = userLocaleRepository.findLocaleByUserIdAndMessengerType(chatDto.getChatId(), chatDto.getMessengerType())
							.orElse(Locale.DEFAULT_LOCALE);
					cacheLocale(chatDto, userLocale);
					return userLocale;
				});
	}

	@Override
	public Locale setUserLocale(ChatIdMessenger chatDto, String localeString) {
		UserLocale entity = userLocaleRepository.findUserLocaleByIdAndMessengerType(chatDto.getChatId(), chatDto.getMessengerType())
				.orElseGet(() -> {
			UserLocale userLocale = new UserLocale();
			userLocale.setChatId(chatDto.getChatId());
			userLocale.setMessengerType(chatDto.getMessengerType());
			return userLocale;
		});
		Locale locale = Locale.valueOf(localeString.toUpperCase());
		entity.setLocale(locale);
		userLocaleRepository.save(entity);
		cacheLocale(chatDto, locale);
		return entity.getLocale();
	}

	private String localeCacheKey(ChatIdMessenger chatDto) {
		return String.format(USER_LOCALE_CACHE_KEY, chatDto.getChatId(), chatDto.getMessengerType());
	}

	private void cacheLocale(ChatIdMessenger chatDto, Locale userLocale) {
		cacheService.addValueExpired(localeCacheKey(chatDto), userLocale);
	}
}
