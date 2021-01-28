package ua.isolutions.bot.commons.messages.message.response.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;

import java.io.InputStream;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileMessage extends AbstractMessage {

	private final InputStream file;
	private final String fileName;
	private final String fileUrl;
	private final Integer fileSize;

	public FileMessage(InputStream file) {
		this.file = file;
		fileName = null;
		fileUrl = null;
		fileSize = null;
	}

	public FileMessage(InputStream file, String fileName) {
		this.file = file;
		this.fileName = fileName;
		fileUrl = null;
		fileSize = null;
	}

	public FileMessage(InputStream file, String fileName, KeyboardWrapper keyboard) {
		this.file = file;
		this.fileName = fileName;
		setKeyboard(keyboard);
		fileUrl = null;
		fileSize = null;
	}

	public FileMessage(InputStream file, KeyboardWrapper keyboard) {
		this.file = file;
		fileName = null;
		setKeyboard(keyboard);
		fileUrl = null;
		fileSize = null;
	}

	public FileMessage(String fileUrl, int fileSize) {
		this.fileUrl = fileUrl;
		this.fileSize = fileSize;
		this.file = null;
		this.fileName = null;
	}

	public FileMessage(String fileUrl, int fileSize, String fileName) {
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.fileSize = fileSize;
		this.file = null;
	}

	public FileMessage(String fileUrl, int fileSize, KeyboardWrapper keyboard) {
		this.fileUrl = fileUrl;
		this.fileSize = fileSize;
		setKeyboard(keyboard);
		this.file = null;
		this.fileName = null;
	}

	public FileMessage(String fileUrl, int fileSize, String fileName, KeyboardWrapper keyboard) {
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.fileSize = fileSize;
		setKeyboard(keyboard);
		this.file = null;
	}
}
