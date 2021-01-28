package ua.isolutions.bot.commons.file.client.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.file.client.FileReceiverClient;
import ua.isolutions.bot.commons.file.dto.FileDto;
import ua.innovations.commons.api.ApiResponse;
import ua.innovations.commons.http.BaseSerializer;
import ua.innovations.commons.http.impl.AbstractHttpClient;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Component
@ConditionalOnProperty(value = {"file.import.picture", "file.download.picture"})
@Log4j2
public class FileReceiverClientImpl extends AbstractHttpClient implements FileReceiverClient {
	private static final String DEFAULT_FILE_NAME = "bot_file";
	private static final String DEFAULT_FILE_EXTENSION = ".tmp";

	@Value("${file.token.url}")
	private String tokenUrl;
	@Value("${file.import.picture}")
	private String importPictureUrl;
	@Value("${file.download.picture}")
	private String downloadPictureUrl;

	public FileReceiverClientImpl(BaseSerializer serializer) {
		super(serializer);
	}

	@Override
	@SneakyThrows
	public FileDto uploadFile(String fileName, InputStream inputStream) {
		String code = getToken();
		String filename = getFileName(fileName);
		HttpEntity httpEntity = getHttpEntity(inputStream, code, filename);
		log.info("Prepared file input stream request for {}", filename);
		log.debug("File input stream request {}", httpEntity);

		FileReceiverResponse response = post(importPictureUrl, httpEntity, FileReceiverResponse.class);
		checkResponse(response);
		return new FileDto(downloadPictureUrl + code, response.getBody().getFileSize());
	}

	private HttpEntity getHttpEntity(InputStream inputStream, String code, String filename) {
		return MultipartEntityBuilder.create()
				.addBinaryBody("file", inputStream, ContentType.APPLICATION_OCTET_STREAM, filename)
				.addTextBody("code", code)
				.addTextBody("filename", getPrefix(filename))
				.addTextBody("fileSuffix", getSuffix(filename))
				.build();
	}

	private void checkResponse(FileReceiverResponse response) {
		if (response.notOk() || response.getBody().getStatus().notSuccessful()) {
			throw new RuntimeException("File wasn't uploaded, response: " + response);
		}
	}

	private String getToken() {
		return get(tokenUrl);
	}

	private String getSuffix(String filename) {
		return filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : DEFAULT_FILE_EXTENSION;
	}

	private String getPrefix(String filename) {
		return filename.contains(".") ? filename.substring(0, filename.lastIndexOf(".")) : DEFAULT_FILE_NAME;
	}

	private String getFileName(String filename) {
		if (Objects.isNull(filename)) {
			return DEFAULT_FILE_NAME + DEFAULT_FILE_EXTENSION;
		}
		filename = filename.contains("/") ? filename.substring(filename.lastIndexOf("/") + 1) : filename;
		filename = filename.contains("?") ? filename.substring(0, filename.indexOf("?")) : filename;
		return filename;
	}

	@Override
	protected Collection<? extends Header> addHeaders() {
		return Collections.emptyList();
	}

	@Data
	private static class FileReceiverFileDto {
		private FileReceiverStatus status;
		private String message;
		private String code;
		private int fileSize;
	}


	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	private static class FileReceiverResponse extends ApiResponse<FileReceiverFileDto> {

		public boolean notOk() {
			return getStatus() != Status.OK;
		}
	}

	private enum FileReceiverStatus {
		SUCCESS("Успішно імпортовано"),
		CODE_NOT_FOUND("Токен не знайдено"),
		CODE_EXPIRED("Вийшов час використання токену"),
		CODE_IS_ALREADY_IN_USE("Токен вже використано"),
		ERROR("Виникла помилка при імпорті файлу"),
		SUCCESSFULLY_REMOVED("Успішно видалено"),
		REMOVE_ERROR("Виникла помилка під час видалення");

		private final String message;

		FileReceiverStatus(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public boolean notSuccessful() {
			return !this.equals(SUCCESS);
		}
	}
}
