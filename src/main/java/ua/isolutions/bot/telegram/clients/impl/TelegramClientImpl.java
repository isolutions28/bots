package ua.isolutions.bot.telegram.clients.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.telegram.clients.TelegramClient;
import ua.isolutions.bot.telegram.clients.dto.FileDetails;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.innovations.commons.http.BaseSerializer;
import ua.innovations.commons.http.impl.AbstractHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

@Component
@Log4j2
@ConditionalOnBean(TelegramBotConfig.class)
public class TelegramClientImpl extends AbstractHttpClient implements TelegramClient {

	@Value("${telegram.get.file.details.url}")
	private String getFileInfoUrl;
	@Value("${telegram.get.file.url}")
	private String getFileUrl;
	@Value("${telegram.bot.token}")
	private String botToken;

	public TelegramClientImpl(BaseSerializer serializer) {
		super(serializer, Level.DEBUG);
	}

	@Override
	public FileDetails getImageDetails(String fileId) {

		String fileDetailsUrl = String.format(getFileInfoUrl, botToken, fileId);

		return get(fileDetailsUrl, FileDetails.class);
	}

	@Override
	public String getImageBase64(String filePath) {

		String fileUrl = String.format(getFileUrl, botToken, filePath);
		ByteArrayInputStream inputStream = getRaw(fileUrl);
		try {
			return Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream));
		} catch (IOException e) {
			log.error("Cannot download file from telegram " + filePath, e.getMessage());
		}
		return null;
	}

	@Override
	protected Collection<? extends Header> addHeaders() {
		return Collections.emptyList();
	}
}
