package ua.isolutions.bot.commons.messages.message.response.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;

import java.io.InputStream;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImageMessage extends FileMessage {

	public ImageMessage(InputStream file) {
		super(file);
	}

	public ImageMessage(InputStream file, String fileName) {
		super(file, fileName);
	}

	public ImageMessage(InputStream file, KeyboardWrapper keyboard) {
		super(file, keyboard);
	}

	public ImageMessage(InputStream file, String fileName, KeyboardWrapper keyboard) {
		super(file, fileName, keyboard);
	}

	public ImageMessage(String fileUrl, int fileSize) {
		super(fileUrl, fileSize);
	}

	public ImageMessage(String fileUrl, int fileSize, String fileName) {
		super(fileUrl, fileSize, fileName);
	}

	public ImageMessage(String fileUrl, int fileSize, KeyboardWrapper keyboard) {
		super(fileUrl, fileSize, keyboard);
	}

	public ImageMessage(String fileUrl, int fileSize, String fileName, KeyboardWrapper keyboard) {
		super(fileUrl, fileSize, fileName, keyboard);
	}
}
