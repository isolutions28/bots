package ua.isolutions.bot.commons.settings.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.cache.CacheService;
import ua.isolutions.bot.commons.settings.dao.SettingsRepository;
import ua.isolutions.bot.commons.settings.domain.Settings;
import ua.isolutions.bot.commons.settings.service.SettingsService;
import ua.isolutions.bot.commons.settings.utils.SettingsConverter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
	private final SettingsRepository settingRepository;
	private final CacheService cacheService;

	@Override
	public String getSetting(String name) {
		return getSetting(name, String::toString);
	}

	@Override
	public <T> T getSetting(String name, SettingsConverter<T> converter) {
		return converter.convert(getCacheValue(name).orElseGet(() -> {
			String value = getDbSetting(name).getValue();
			cacheService.addStringValueExpired(name, value);
			return value;
		}));
	}

	private Optional<String> getCacheValue(String name) {
		return cacheService.get(name);
	}

	private Settings getDbSetting(String name) {
		return settingRepository.findById(name)
				.orElseThrow(() -> new RuntimeException("Setting not found: '" + name + "'"));
	}

}
