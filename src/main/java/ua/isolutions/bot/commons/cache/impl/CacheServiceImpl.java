package ua.isolutions.bot.commons.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import ua.isolutions.bot.commons.cache.CacheService;
import ua.isolutions.bot.commons.sting_generator.StringGeneratorService;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {
	private static final String DIVIDER = "___";
	private final JedisPool jedisPool;
	private final StringGeneratorService stringGenerator;
	private final ObjectMapper serializer;
	@Value("${app.name.key}")
	private String appName;
	@Value("${default.cache.lifetime.seconds}")
	private int defaultCacheLifetimeSeconds;

	@Override
	public String addSerializedObject(Object object) {
		String key = stringGenerator.charsString(25);
		return addSerializedObject(key, object);
	}

	@Override
	@SneakyThrows
	public String addSerializedObject(String key, Object object) {
		String serialized = serialize(object);
		addValue(key, serialized);
		return key;
	}

	private String serialize(Object object) throws JsonProcessingException {
		return serializer.writeValueAsString(object);
	}

	@Override
	public String setExpiretionTime(String key, int seconds) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.expire(key, seconds);
		}
		return key;
	}

	@Override
	public String addValue(String key, String value) {
		if (log.isDebugEnabled()) log.debug("Cache add key: " + generateKey(key) + ", value: " + value);
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.set(generateKey(key), value);
		}
	}

	@Override
	public String addValueExpired(String key, String value, int seconds) {
		if (log.isDebugEnabled()) log.debug("Cache add expired key: " + generateKey(key) + ", value: " + value + ", expired: " + seconds);
		String res = addValue(key, value);
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.expire(generateKey(key), seconds);
		}
		return res;
	}

	@Override
	@SneakyThrows
	public String addValueExpired(String key, Object value, int seconds) {
		String serializedValue = serialize(value);
		return addValueExpired(key, serializedValue, seconds);
	}

	@Override
	public String addValueExpired(String key, Object value) {
		return addValueExpired(key, value, defaultCacheLifetimeSeconds);
	}

	@Override
	public String addStringValueExpired(String key, String value) {
		return addValueExpired(key, value, defaultCacheLifetimeSeconds);
	}

	@Override
	public Optional<String> get(String key) {
		if (log.isDebugEnabled()) log.debug("Get cache key: " + generateKey(key));
		try (Jedis jedis = jedisPool.getResource()) {
			Optional<String> result = Optional.ofNullable(jedis.get(generateKey(key)));
			if (log.isDebugEnabled()) log.debug("Value from redis. Key: " + generateKey(key) + ", value: " + result);
			return result;
		}
	}

	@Override
	public <T> Optional<T> get(String key, Class<T> tClass) {
		if (log.isDebugEnabled()) log.debug("Get cache key: " + generateKey(key) + " of type: " + tClass.getName());
		try {
			Optional<String> serializedObjectOpt = get(key);
			String serializedValue = serializedObjectOpt.orElseThrow(() -> new RuntimeException("No value found! " + generateKey(key)));
			Optional<T> deserialize = Optional.of(deserialize(tClass, serializedValue));
			if (log.isDebugEnabled()) log.debug("Value from redis. Key: " + generateKey(key) + ", value: " + deserialize);
			return deserialize;
		} catch (Exception e) {
			log.warn("Can't get val from redis! " + generateKey(key), e);
			return Optional.empty();
		}
	}

	@Override
	public <T> Optional<T> get(String key, TypeReference<T> reference) {
		if (log.isDebugEnabled()) log.debug("Get cache key: " + generateKey(key) + " of type: " + reference.getType().getTypeName());
		try {
			Optional<String> serializedObjectOpt = get(key);
			String serializedValue = serializedObjectOpt.orElseThrow(() -> new RuntimeException("No value found! " + generateKey(key)));
			Optional<T> deserialize = Optional.of(serializer.readValue(serializedValue, reference));
			if (log.isDebugEnabled()) log.debug("Value from redis. Key: " + generateKey(key) + ", value: " + deserialize);
			return deserialize;
		} catch (Exception e) {
			log.warn("Can't get val from redis! " + generateKey(key), e);
			return Optional.empty();
		}
	}

	private <T> T deserialize(Class<T> tClass, String serializedValue) throws java.io.IOException {
		return serializer.readValue(serializedValue, tClass);
	}

	@Override
	public <T> Optional<T> getSilent(String key, Class<T> tClass) {
		if (log.isDebugEnabled()) log.debug("Get cache silently. Key: " + generateKey(key) + " of type: " + tClass.getName());
		try {
			Optional<String> serializedObjectOpt = get(key);
			String serializedValue = serializedObjectOpt.orElseThrow(() -> new RuntimeException("No value found! " + generateKey(key)));
			return Optional.of(deserialize(tClass, serializedValue));
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Can't get val from redis! " + generateKey(key), e);
			return Optional.empty();
		}
	}

	@Override
	public <T> Optional<T> getSilent(String key, TypeReference<T> reference) {
		if (log.isDebugEnabled()) log.debug("Get cache silently. Key: " + generateKey(key) + " of reference: " + reference.getType().getTypeName());
		try {
			Optional<String> serializedObjectOpt = get(key);
			String serializedValue = serializedObjectOpt.orElseThrow(() -> new RuntimeException("No value found! " + generateKey(key)));
			return Optional.of(serializer.readValue(serializedValue, reference));
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("Can't get val from redis! " + generateKey(key), e);
			return Optional.empty();
		}
	}

	@Override
	public boolean remove(String key) {
		if (log.isDebugEnabled()) log.debug("Remove value from cache. Key: " + generateKey(key));
		try (Jedis jedis = jedisPool.getResource()) {
			boolean result = jedis.del(generateKey(key)) != 0;
			if (log.isDebugEnabled()) log.debug("Remove value from cache. Key: " + generateKey(key) + ", result: " + result);
			return result;
		}
	}

	private String generateKey(String key) {
		return this.appName + DIVIDER + key;
	}
}
