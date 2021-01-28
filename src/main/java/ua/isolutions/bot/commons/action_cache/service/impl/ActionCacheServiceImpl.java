package ua.isolutions.bot.commons.action_cache.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.action_cache.dto.CacheIdentifier;
import ua.isolutions.bot.commons.action_cache.service.ActionCacheService;
import ua.isolutions.bot.commons.cache.CacheService;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ActionCacheServiceImpl implements ActionCacheService {

	private static final String ACTION_CACHE_PREFIX = "action.cache.";
	private static final TypeReference<Map<String, Object>> CACHE_REFERENCE = new TypeReference<Map<String, Object>>() {};

	private final CacheService cacheService;

	@Override
	public void clearCache(ChatIdMessenger userId) {
		cacheService.addSerializedObject(getMapKey(userId), new HashMap<String, Object>());
	}

	@Override
	public void add(ChatIdMessenger userId, String key, Object o) {
		Map<String, Object> cacheMap = getCacheMap(userId);
		cacheMap.put(key, o);
		cacheService.addSerializedObject(getMapKey(userId), cacheMap);
	}

	@Override
	public void add(ChatIdMessenger userId, CacheIdentifier key, Object o) {
		add(userId, key.name(), o);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(ChatIdMessenger userId, String key) {
		Map<String, Object> map = getCacheMap(userId);
		return (T) map.get(key);
	}

	@Override
	public <T> T get(ChatIdMessenger userId, CacheIdentifier key) {
		return get(userId, key.name());
	}

	@Override
	public void remove(ChatIdMessenger userId, String key) {
		Map<String, Object> cacheMap = getCacheMap(userId);
		cacheMap.remove(key);
		cacheService.addSerializedObject(getMapKey(userId), cacheMap);
	}

	private Map<String, Object> getCacheMap(ChatIdMessenger userId) {
		return cacheService.getSilent(getMapKey(userId), CACHE_REFERENCE)
				.orElseGet(HashMap::new);
	}

	private String getMapKey(ChatIdMessenger userId) {
		return ACTION_CACHE_PREFIX + userId.getMessengerType() + "." + userId.getChatId();
	}
}
