package ua.isolutions.bot.commons.messages.message.incoming;

import lombok.Data;

import java.io.InputStream;

@Data
public class MessengerFileWrapper {
	private final String fileName;
	private final String fileUrl;
	private final InputStream fileStream;

	public MessengerFileWrapper(String fileName, String fileUrl) {
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.fileStream = null;

	}

	public MessengerFileWrapper(String fileName, InputStream fileStream) {
		this.fileName = fileName;
		this.fileStream = fileStream;
		this.fileUrl = null;
	}
}
