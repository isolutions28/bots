package ua.isolutions.bot.commons.cache;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Optional;

public interface CacheService extends ua.innovations.commons.cache.CacheService {
	String addSerializedObject(Object object);

	String addSerializedObject(String key, Object object);

	String setExpiretionTime(String key, int seconds);

	String addValue(String key, String value);

	String addValueExpired(String key, String value, int seconds);

	String addValueExpired(String key, Object value, int seconds);

	String addValueExpired(String key, Object value);

	String addStringValueExpired(String key, String value);

	Optional<String> get(String key);

	<T> Optional<T> get(String key, Class<T> tClass);

	<T> Optional<T> get(String key, TypeReference<T> reference);

	boolean remove(String key);

	<T> Optional<T> getSilent(String key, Class<T> tClass);

	<T> Optional<T> getSilent(String key, TypeReference<T> reference);
}

