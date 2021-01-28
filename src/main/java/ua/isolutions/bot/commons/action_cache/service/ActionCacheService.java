package ua.isolutions.bot.commons.action_cache.service;


import ua.isolutions.bot.commons.action_cache.dto.CacheIdentifier;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;

public interface ActionCacheService {

	void clearCache(ChatIdMessenger userId);

	void add(ChatIdMessenger userId, String key, Object o);

	void add(ChatIdMessenger userId, CacheIdentifier key, Object o);

	<T> T get(ChatIdMessenger userId, String key);

	<T> T get(ChatIdMessenger userId, CacheIdentifier key);

	void remove(ChatIdMessenger userId, String key);
}
