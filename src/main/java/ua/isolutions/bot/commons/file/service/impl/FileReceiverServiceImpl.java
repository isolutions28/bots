package ua.isolutions.bot.commons.file.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import ua.isolutions.bot.commons.file.client.FileReceiverClient;
import ua.isolutions.bot.commons.file.dto.FileDto;
import ua.isolutions.bot.commons.file.service.FileReceiverService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

@Service
@ConditionalOnBean(FileReceiverClient.class)
@RequiredArgsConstructor
@Log4j2
public class FileReceiverServiceImpl implements FileReceiverService {

	private final FileReceiverClient fileReceiverClient;

	@Override
	public FileDto uploadFile(String filename, InputStream inputStream) {
		try {
			log.info("Upload file request: {}", filename);
			FileDto fileDto = fileReceiverClient.uploadFile(filename, inputStream);
			log.info("Upload file response. Filename: {}, response: {}", filename, fileDto);
			return fileDto;
		} catch (Exception e) {
			log.error("Cannot upload file to receiver. Name: " + filename, e);
			throw new RuntimeException("Upload failed. " + filename, e);
		}
	}

	@Override
	public InputStream downloadFile(String url) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			IOUtils.copy(new URL(url).openStream(), outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (Exception e) {
			log.error("Cannot download file: " + url, e);
			throw new RuntimeException("Download failed " + url, e);
		}
	}
}
