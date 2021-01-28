package ua.isolutions.bot.commons.messages.message.response.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;

import java.io.InputStream;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AudioMessage extends FileMessage {

	public AudioMessage(InputStream file) {
		super(file);
	}

	public AudioMessage(InputStream file, String fileName) {
		super(file, fileName);
	}

	public AudioMessage(InputStream file, String fileName, KeyboardWrapper keyboard) {
		super(file, fileName, keyboard);
	}

	public AudioMessage(InputStream file, KeyboardWrapper keyboard) {
		super(file, keyboard);
	}

	public AudioMessage(String fileUrl, int fileSize) {
		super(fileUrl, fileSize);
	}

	public AudioMessage(String fileUrl, int fileSize, String fileName) {
		super(fileUrl, fileSize, fileName);
	}

	public AudioMessage(String fileUrl, int fileSize, KeyboardWrapper keyboard) {
		super(fileUrl, fileSize, keyboard);
	}

	public AudioMessage(String fileUrl, int fileSize, String fileName, KeyboardWrapper keyboard) {
		super(fileUrl, fileSize, fileName, keyboard);
	}
}
