package ua.isolutions.bot.commons.file.dto;

import lombok.Data;

@Data
public class FileDto {
	private final String downloadUrl;
	private final int fileSize;
}
