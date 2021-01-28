package ua.isolutions.bot.commons.file.client;

import ua.isolutions.bot.commons.file.dto.FileDto;

import java.io.InputStream;

public interface FileReceiverClient {
	FileDto uploadFile(String fileName, InputStream inputStream);
}
