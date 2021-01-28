package ua.isolutions.bot.commons.file.service;

import ua.isolutions.bot.commons.file.dto.FileDto;

import java.io.InputStream;

public interface FileReceiverService {
	FileDto uploadFile(String filename, InputStream inputStream);

	InputStream downloadFile(String url);
}
