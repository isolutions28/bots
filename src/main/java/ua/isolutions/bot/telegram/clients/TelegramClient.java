package ua.isolutions.bot.telegram.clients;

import ua.isolutions.bot.telegram.clients.dto.FileDetails;

public interface TelegramClient {

	FileDetails getImageDetails(String fileId);
	String getImageBase64(String filePath);
}
